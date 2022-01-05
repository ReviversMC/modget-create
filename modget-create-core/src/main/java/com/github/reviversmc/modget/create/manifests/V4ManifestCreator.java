package com.github.reviversmc.modget.create.manifests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.create.apicalls.ModrinthQuery;
import com.github.reviversmc.modget.create.apicalls.ModrinthQueryFactory;
import com.github.reviversmc.modget.create.data.*;
import com.github.reviversmc.modget.library.manager.RepoManager;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.main.ModAuthor;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.main.ModManifest;
import com.github.reviversmc.modget.manifests.spec4.api.data.mod.ModPackage;
import com.github.reviversmc.modget.manifests.spec4.api.data.ManifestRepository;
import com.github.reviversmc.modget.manifests.spec4.api.data.lookuptable.LookupTable;
import com.github.reviversmc.modget.manifests.spec4.api.data.lookuptable.LookupTableEntry;
import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import okhttp3.*;

import javax.inject.Named;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class V4ManifestCreator implements ManifestCreator {
    private final FabricModPojo modPojo;
    private final int curseforgeId;
    private final List<String> updateAlternatives;
    private final ManifestV4MainPojo manifestV4MainPojo;
    private final ModrinthQuery modrinthQuery;
    private final ModStatus modStatus;
    private final RepoManager repoManager;
    private final String authToken; //Allow for use of custom token.
    private final String wiki;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper yamlMapper;

    @AssistedInject
    public V4ManifestCreator(
            @Assisted List<String> updateAlternatives,
            ManifestV4MainPojo manifestV4MainPojo,
            ModrinthQueryFactory modrinthQueryFactory,
            @Assisted ModStatus modStatus,
            RepoManager repoManager,
            @Assisted("authToken") String authToken,
            @Assisted("curseId") String curseforgeId,
            @Assisted("modJarPath") String modJarPath,
            @Assisted("modrinthId") String modrinthId,
            @Assisted("wiki") String wiki,
            @Named("json") ObjectMapper jsonMapper,
            @Named("yaml") ObjectMapper yamlMapper,
            OkHttpClient okHttpClient
    ) {
        this.updateAlternatives = updateAlternatives;
        this.manifestV4MainPojo = manifestV4MainPojo;
        this.modrinthQuery = modrinthQueryFactory.create(modrinthId);
        this.modStatus = modStatus;
        this.repoManager = repoManager;
        this.authToken = authToken;
        this.wiki = wiki;

        int tempCurseforgeId = -1;
        try {
            tempCurseforgeId = Integer.parseInt(curseforgeId);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        this.curseforgeId = tempCurseforgeId;

        this.okHttpClient = okHttpClient;
        this.yamlMapper = yamlMapper;

        if (modJarPath.startsWith("\"") || modJarPath.startsWith("'")) {
            modJarPath = modJarPath.substring(1);
        }

        if (modJarPath.endsWith("\"") || modJarPath.endsWith("'")) {
            modJarPath = modJarPath.substring(0, modJarPath.length() - 1);
        }

        File mod = new File(modJarPath);
        FabricModPojo tempModPojo = null;
        if (mod.exists() && modJarPath.toLowerCase().endsWith(".jar")) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{mod.toURI().toURL()});
                InputStream modJsonStream = classLoader.getResourceAsStream("fabric.mod.json");

                if (modJsonStream != null) {
                    tempModPojo = jsonMapper.readValue(modJsonStream, FabricModPojo.class);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Let modPojo be null if mod doesn't exist, instead of empty class.
        modPojo = tempModPojo;

    }

    @Override
    public Optional<String> createMainYaml() {
        return createMainYaml(false);
    }

    @Override
    public Optional<String> createMainYaml(boolean forceCreate) {

        if (!isUsable()) return Optional.empty();
        if (!forceCreate && isModPresent()) return Optional.empty();

        //noinspection HttpUrlsUsage
        boolean githubFound = modPojo.getContact().getSources().toLowerCase().startsWith("http://github.com/")
                || modPojo.getContact().getSources().toLowerCase().startsWith("https://github.com/");

        boolean curseforgeFound;
        Optional<CurseProject> optionalCurseProject;

        try {
            optionalCurseProject = CurseAPI.project(curseforgeId);
            curseforgeFound = optionalCurseProject.isPresent();
        } catch (CurseException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }

        boolean modrinthFound = modrinthQuery.modExists();
        Optional<ModrinthV1ModPojo> optionalModrinthV1ModPojo = modrinthQuery.getMod();
        if (modrinthFound && optionalModrinthV1ModPojo.isEmpty()) return Optional.empty(); //Should never happen.

        //Set publisher
        if (githubFound) {
            //Format is either http:// github.com/Publisher/repo or https://github/Publisher/repo.
            String githubPage = modPojo.getContact().getSources();


            //noinspection HttpUrlsUsage
            if (githubPage.toLowerCase().startsWith("http://github.com/"))
                manifestV4MainPojo.setPublisher(githubPage.substring(18).split("/")[0]);

            else manifestV4MainPojo.setPublisher(githubPage.substring(19).split("/")[0]);

        } else if (curseforgeFound)
            manifestV4MainPojo.setPublisher(optionalCurseProject.get().author().name());

        else if (modrinthFound) {
            Optional<ModrinthV1UserPojo> optionalModOwner = modrinthQuery.getOwner();
            if (optionalModOwner.isPresent())
                manifestV4MainPojo.setPublisher(optionalModOwner.get().getName());
            else return Optional.empty(); //Should never happen.
        } else //Don't bother creating a manifest if it is not listed on either of the above three platforms.
            return Optional.empty();

        //For icons, they are all non-mutually exclusive.
        List<String> iconUrls = new ArrayList<>();

        if (modrinthFound)
            optionalModrinthV1ModPojo.get().getIconUrl().ifPresent(iconUrls::add);

        if (curseforgeFound) {
            HttpUrl iconUrl = optionalCurseProject.get().logo().url();
            if (iconUrl != null) iconUrls.add(iconUrl.toString());
        }

        //GitHub check is welcome, but there may not be a consistent way to find icon.pngs.
        manifestV4MainPojo.setIconUrls(iconUrls.toArray(new String[0]));

        manifestV4MainPojo.setStatus(modStatus.name().toLowerCase());

        List<ManifestV4MainPojo.UpdateAlternative> updateAlternativeList = new ArrayList<>();
        for (String updateAlternativeCandidate : updateAlternatives) {
            ManifestV4MainPojo.UpdateAlternative updateAlternative = new ManifestV4MainPojo.UpdateAlternative();
            updateAlternative.setPackageId(updateAlternativeCandidate);
            updateAlternativeList.add(updateAlternative);
        }

        manifestV4MainPojo.setUpdateAlternatives(
                updateAlternativeList.toArray(new ManifestV4MainPojo.UpdateAlternative[0])
        );

        manifestV4MainPojo.setName(modPojo.getName());
        manifestV4MainPojo.setDescription(modPojo.getDescription());

        List<ManifestV4MainPojo.Author> authorList = new ArrayList<>();
        for (String authorCandidate : modPojo.getAuthors()) {
            ManifestV4MainPojo.Author author = new ManifestV4MainPojo.Author();
            author.setName(authorCandidate);
            authorList.add(author);
        }

        manifestV4MainPojo.setAuthors(authorList.toArray(new ManifestV4MainPojo.Author[0]));

        manifestV4MainPojo.setHome(modPojo.getContact().getHomepage());
        manifestV4MainPojo.setSource(modPojo.getContact().getSources());
        manifestV4MainPojo.setIssues(modPojo.getContact().getIssues());

        manifestV4MainPojo.setWiki(wiki);

        ManifestV4MainPojo.Chats chats = new ManifestV4MainPojo.Chats();
        chats.setIrc(modPojo.getContact().getIrc());

        List<ManifestV4MainPojo.Chats.MiscChat> miscChats = new ArrayList<>();

        modPojo.getContact().getOthers().forEach(
                (String key, Object value) -> {
                    if (key.equalsIgnoreCase("discord")) chats.setDiscord(value.toString());
                    else {
                        ManifestV4MainPojo.Chats.MiscChat miscChat = new ManifestV4MainPojo.Chats.MiscChat();
                        miscChat.setName(key);
                        miscChat.setUrl(value.toString());
                        miscChats.add(miscChat);
                    }
                }
        );

        if (!miscChats.isEmpty()) chats.setOthers(miscChats.toArray(new ManifestV4MainPojo.Chats.MiscChat[0]));
        else chats.setOthers(null); //Will be fixed later, through String#replace.

        if (modrinthFound) {
            if (optionalModrinthV1ModPojo.get().getDiscordUrl().isPresent())
                chats.setDiscord(optionalModrinthV1ModPojo.get().getDiscordUrl().get());
        }

        manifestV4MainPojo.setChats(chats);

        if (!manifestV4MainPojo.getChats().getDiscord().equalsIgnoreCase("~"))
            manifestV4MainPojo.setSupport(manifestV4MainPojo.getChats().getDiscord());
        else if (!manifestV4MainPojo.getChats().getIrc().equalsIgnoreCase("~"))
            manifestV4MainPojo.setSupport(manifestV4MainPojo.getChats().getIrc());
        else if (manifestV4MainPojo.getChats().getOthers().length != 0)
            manifestV4MainPojo.setSupport(manifestV4MainPojo.getChats().getOthers()[0].getUrl());
        else manifestV4MainPojo.setSupport(manifestV4MainPojo.getIssues()); //Automatically Tilde if NA.

        //TODO Set versions from CF/Modrinth.
        manifestV4MainPojo.setVersions(null);

        try {
            return Optional.of(
                    yamlMapper.writeValueAsString(manifestV4MainPojo)
                            /*
                            Make it so that Tilde is never in quotes, but all other strings are.
                            Example format:

                            fieldA: "some value"
                            fieldB: ~
                            */
                            .replace(": \"~\"", ": ~")

                            //Fix issue of chats.others being represented incorrectly as null.
                            .replace("others: null", "others: ~")
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public boolean isModPresent() {
        try {
            ManifestRepository mainManifestRepo = repoManager.getRepo(0);

            LookupTable mainRepoTable = mainManifestRepo.getOrDownloadLookupTable();
            List<LookupTableEntry> lookupTableEntries = mainRepoTable.getOrDownloadEntries();

            for (LookupTableEntry entry : lookupTableEntries) {
                if (entry.getId().equals(modPojo.getId())) {
                    for (ModPackage modPackage : entry.getOrDownloadPackages()) {
                        for (ModManifest modManifest : modPackage.getOrDownloadManifests(List.of(mainManifestRepo))) {
                            for (ModAuthor modAuthor : modManifest.getAuthors()) {
                                for (String pojoAuthorName : modPojo.getAuthors()) {
                                    if (modAuthor.getName().equals(pojoAuthorName)) return true;
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isUsable() {
        return modPojo != null;
    }
}
