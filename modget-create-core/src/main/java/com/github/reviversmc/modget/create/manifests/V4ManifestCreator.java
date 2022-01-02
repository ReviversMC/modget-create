package com.github.reviversmc.modget.create.manifests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.create.apicalls.ModrinthQuery;
import com.github.reviversmc.modget.create.apicalls.ModrinthQueryFactory;
import com.github.reviversmc.modget.create.data.FabricModPojo;
import com.github.reviversmc.modget.create.data.ManifestV4MainPojo;
import com.github.reviversmc.modget.create.data.ModStatus;
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
    private final List<String> updateAlternatives;
    private final ManifestV4MainPojo manifestV4MainPojo;
    private final ModrinthQuery modrinthQuery;
    private final ModStatus modStatus;
    private final RepoManager repoManager;
    private final String authToken; //Allow for use of custom token.
    private final int curseforgeId;
    private final String modrinthId;
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

        int tempCurseforgeId = -1;
        try {
            tempCurseforgeId = Integer.parseInt(curseforgeId);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        this.curseforgeId = tempCurseforgeId;

        this.modrinthId = modrinthId;
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

        if (!isUsable() || isModPresent()) return Optional.empty();

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

        else if (modrinthFound)
            manifestV4MainPojo.setPublisher(modrinthQuery.getOwner().getName());

        else //Don't bother creating a manifest if it is not listed on either of the above three platforms.
            return Optional.empty();

        //For icons, they are all non-mutually exclusive.
        List<String> iconUrls = new ArrayList<>();

        if (modrinthFound)
            modrinthQuery.getMod().getIconUrl().ifPresent(iconUrls::add);

        if (curseforgeFound) {
            HttpUrl iconUrl = optionalCurseProject.get().logo().url();
            if (iconUrl != null) iconUrls.add(iconUrl.toString());
        }

        if (githubFound) {
            //TODO Use api calls to check for an icon.
        }

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

        /*
        Get home either from mod.json gh, or mr page, or cf page.
         */

        manifestV4MainPojo.setSource(modPojo.getContact().getSources());
        manifestV4MainPojo.setIssues(modPojo.getContact().getIssues());


        //TODO Maybe one day find out a way to do support? Disc first, next is issues.
        manifestV4MainPojo.setSupport(null);

        //TODO Check if wiki exists via graphql call.
        manifestV4MainPojo.setWiki(null);

        //TODO Get chats from CF/Modrinth.
        manifestV4MainPojo.setChats(null);

        //TODO Set versions from CF/Modrinth.
        manifestV4MainPojo.setVersions(null);

        try {
            return Optional.of(yamlMapper.writeValueAsString(manifestV4MainPojo));
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
