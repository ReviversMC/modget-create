package com.github.reviversmc.modget.create.manifests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.create.data.FabricModPojo;
import com.github.reviversmc.modget.create.data.ManifestV4;
import com.github.reviversmc.modget.create.data.ModStatus;
import com.github.reviversmc.modget.library.manager.RepoManager;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.main.ModAuthor;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.main.ModManifest;
import com.github.reviversmc.modget.manifests.spec4.api.data.mod.ModPackage;
import com.github.reviversmc.modget.manifests.spec4.api.data.ManifestRepository;
import com.github.reviversmc.modget.manifests.spec4.api.data.lookuptable.LookupTable;
import com.github.reviversmc.modget.manifests.spec4.api.data.lookuptable.LookupTableEntry;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import okhttp3.*;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;

public class V4ManifestCreator implements ManifestCreator {
    private final FabricModPojo modPojo;
    private final ManifestV4 manifestV4;
    private final ModStatus modStatus;
    private final RepoManager repoManager;
    private final String authToken; //Allow for use of custom token.
    private final String curseforgeId;
    private final String modrinthId;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper yamlMapper;

    @AssistedInject
    public V4ManifestCreator(
            ManifestV4 manifestV4,
            ModStatus modStatus,
            RepoManager repoManager,
            @Assisted("authToken") String authToken,
            @Assisted("curseId") String curseforgeId,
            @Assisted("modJarPath") String modJarPath,
            @Assisted("modrinthID") String modrinthId,
            @Named("json") ObjectMapper jsonMapper,
            @Named("yaml") ObjectMapper yamlMapper,
            OkHttpClient okHttpClient
    ) {
        this.manifestV4 = manifestV4;
        this.modStatus = modStatus;
        this.repoManager = repoManager;
        this.authToken = authToken;
        this.curseforgeId = curseforgeId;
        this.modrinthId = modrinthId;
        this.okHttpClient = okHttpClient;
        this.yamlMapper = yamlMapper;

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
    public Optional<OutputStream> createMainYaml() {

        if (!isUsable() || isModPresent()) return Optional.empty();

        /*
        Set through api calls:
        - Publisher
        - Icon Urls
         */

        manifestV4.setStatus(modStatus.name().toLowerCase());



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
