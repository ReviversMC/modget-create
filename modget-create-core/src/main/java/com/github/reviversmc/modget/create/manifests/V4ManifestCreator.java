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
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.connector.GitHubConnector;

import javax.inject.Named;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class V4ManifestCreator implements ManifestCreator {
    private final FabricModPojo modPojo;
    private final GitHub githubAPI;
    private final int curseforgeId;
    private final List<String> modVersions;
    private final List<String> updateAlternatives;
    private final ManifestV4MainPojo manifestV4MainPojo;
    private final ModrinthQuery modrinthQuery;
    private final ModStatus modStatus;
    private final RepoManager repoManager;
    private final String wiki;
    private final ObjectMapper yamlMapper;

    private final boolean isUsable;

    @AssistedInject
    public V4ManifestCreator(
            GitHubConnector gitHubConnector,
            @Assisted("modVersions") List<String> modVersions,
            @Assisted("updateAlternatives") List<String> updateAlternatives,
            ManifestV4MainPojo manifestV4MainPojo,
            ModrinthQueryFactory modrinthQueryFactory,
            @Assisted ModStatus modStatus,
            RepoManager repoManager,
            @Assisted("authToken") String authToken, //Null value is fine.
            @Assisted("curseId") String curseforgeId,
            @Assisted("modJarPath") String modJarPath,
            @Assisted("modrinthId") String modrinthId,
            @Assisted("wiki") String wiki,
            @Named("json") ObjectMapper jsonMapper,
            @Named("yaml") ObjectMapper yamlMapper
    ) {
        this.modVersions = modVersions;
        this.updateAlternatives = updateAlternatives;
        this.manifestV4MainPojo = manifestV4MainPojo;
        this.modrinthQuery = modrinthQueryFactory.create(modrinthId);
        this.modStatus = modStatus;
        this.repoManager = repoManager;
        this.wiki = wiki;
        this.yamlMapper = yamlMapper;

        Properties authProperties = new Properties();
        if (authToken != null)
            authProperties.setProperty("oauth", authToken);

        GitHub tempGithubAPI;
        try {
            tempGithubAPI = GitHubBuilder.fromProperties(authProperties)
                    .withConnector(gitHubConnector)
                    .build();

        } catch (IOException ex) {
            ex.printStackTrace();
            tempGithubAPI = null;
        }

        this.githubAPI = tempGithubAPI;


        int tempCurseforgeId = -1;
        try {
            tempCurseforgeId = Integer.parseInt(curseforgeId);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        this.curseforgeId = tempCurseforgeId;


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
                    tempModPojo =
                            jsonMapper.readValue(modJsonStream, FabricModPojo.class);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Let modPojo be null if mod doesn't exist, instead of empty class.
        modPojo = tempModPojo;

        isUsable = githubAPI != null && githubAPI.isCredentialValid() && modPojo != null;

    }

    /**
     * Cross-references the mod id in its mod.fabric.json with the nameCandidate given.
     *
     * @param nameCandidate The name to cross-reference with.
     * @return The kebab-cased name if it is different.
     */
    private Optional<String> createAlternateName(String nameCandidate) {
        String alphaNumericName = nameCandidate.replaceAll("[^A-Za-z0-9]", "");

        /*
          If alphaNumericName is "GoodMod" and id is "goodmod", we need to create alt name of "good-mod".
          However, this can create false positives.
          If the alphaNumericName is "Mod" and the id is "mod", a false positive alt name "mod" will be created.
         */
        if (!nameCandidate.equals(modPojo.getId())) {
            String kebabCasedName = alphaNumericName
                    .replaceAll("(?<=[a-z])([A-Z])", "-$1") //Turns "ModName" into "Mod-Name"
                    .toLowerCase();

            if (!modPojo.getId().equals(kebabCasedName)) //Filter out false positives.
                return Optional.of(kebabCasedName);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> createLookupTable() throws CurseException, IOException {
        return createLookupTable(false);
    }

    @Override
    public Optional<String> createLookupTable(boolean forceCreate) throws CurseException, IOException {

        ManifestV4LookupTablePojo[] manifestV4LookupTablePojos;


        InputStream lookupTableStream = githubAPI.getRepository("ReviversMc/modget-manifests")
                .getFileContent("lookup-table.yaml")
                .read();

        manifestV4LookupTablePojos =
                yamlMapper.readValue(lookupTableStream, ManifestV4LookupTablePojo[].class);


        ManifestV4LookupTablePojo modEntry = new ManifestV4LookupTablePojo();
        modEntry.setId(modPojo.getId().toLowerCase());

        //TODO Fill in Alt names, Packages, and Tags.
        List<String> alternateNames = new ArrayList<>();

        createAlternateName(modPojo.getName()).ifPresent(alternateNames::add); //No value alr in alt name list.


        Optional<CurseProject> optionalCurseProject = CurseAPI.project(curseforgeId);
        Optional<ModrinthV1ModPojo> optionalModrinthV1ModPojo = modrinthQuery.getMod();

        optionalCurseProject.flatMap(curseProject ->
                createAlternateName(curseProject.name())).ifPresent(altName -> {
            if (!alternateNames.contains(altName)) alternateNames.add(altName);
        });

        optionalModrinthV1ModPojo.flatMap(modrinthV1ModPojo ->
                createAlternateName(modrinthV1ModPojo.getTitle())).ifPresent(altName -> {
            if (!alternateNames.contains(altName)) alternateNames.add(altName);
        });

        if (alternateNames.isEmpty()) modEntry.setAlternativeNames(new String[]{"~"});
        else modEntry.setAlternativeNames(alternateNames.toArray(new String[0]));


        ManifestV4LookupTablePojo[] editedManifestV4LookupTablePojos
                = new ManifestV4LookupTablePojo[manifestV4LookupTablePojos.length + 1];

        for (int x = 0; x < manifestV4LookupTablePojos.length; x++) {

            //Replace the entry that we are overriding, if applicable.
            if (forceCreate &&
                    manifestV4LookupTablePojos[x].getId().equalsIgnoreCase(modEntry.getId())) continue;

            if (x != 0 && editedManifestV4LookupTablePojos[x] == null)
                editedManifestV4LookupTablePojos[x - 1] = manifestV4LookupTablePojos[x];
            else
                editedManifestV4LookupTablePojos[x] = manifestV4LookupTablePojos[x];
        }

        editedManifestV4LookupTablePojos[editedManifestV4LookupTablePojos.length - 1] = modEntry;


        return Optional.of(
                yamlMapper.writeValueAsString(editedManifestV4LookupTablePojos)
                        /*
                        Make it so that Tilde is never in quotes, but all other strings are.
                        Example format:

                        fieldA: "some value"
                        fieldB: ~
                        */
                        .replace(": \"~\"", ": ~")
        );

    }

    @Override
    public Optional<String> createMainYaml() throws Exception {
        return createMainYaml(false);
    }

    @Override
    public Optional<String> createMainYaml(boolean forceCreate) throws Exception {

        if (!isUsable()) return Optional.empty();
        if (!forceCreate && isModPresent()) return Optional.empty();

        //noinspection HttpUrlsUsage
        boolean githubFound = modPojo.getContact().getSources().toLowerCase().startsWith("http://github.com/")
                || modPojo.getContact().getSources().toLowerCase().startsWith("https://github.com/");

        Optional<CurseProject> optionalCurseProject = CurseAPI.project(curseforgeId);
        boolean curseforgeFound = optionalCurseProject.isPresent();

        Optional<ModrinthV1ModPojo> optionalModrinthV1ModPojo = modrinthQuery.getMod();
        boolean modrinthFound = optionalModrinthV1ModPojo.isPresent();

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
        chats.setDiscord("~"); //Can be changed later, if a value is found.
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
        else if (manifestV4MainPojo.getChats().getOthers() != null &&
                manifestV4MainPojo.getChats().getOthers().length != 0) {

            manifestV4MainPojo.setSupport(manifestV4MainPojo.getChats().getOthers()[0].getUrl());

        } else manifestV4MainPojo.setSupport(manifestV4MainPojo.getIssues()); //Automatically Tilde if NA.

        List<ManifestV4MainPojo.Version> modVersionList = new ArrayList<>();
        for (String modVersionCandidate : modVersions) {
            ManifestV4MainPojo.Version modVersion = new ManifestV4MainPojo.Version();
            modVersion.setVersion(modVersionCandidate);
            modVersionList.add(modVersion);
        }

        manifestV4MainPojo.setVersions(modVersionList.toArray(new ManifestV4MainPojo.Version[0]));


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

    }


    @Override
    public boolean isModPresent() throws Exception {
        if (!isUsable()) return false;

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

        return false;
    }

    @Override
    public boolean isUsable() {
        return isUsable;
    }
}
