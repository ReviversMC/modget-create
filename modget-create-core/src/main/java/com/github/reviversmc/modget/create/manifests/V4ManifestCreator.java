package com.github.reviversmc.modget.create.manifests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.create.data.FabricModPojo;
import com.github.reviversmc.modget.create.data.ManifestV4;
import com.github.reviversmc.modget.create.data.ModStatus;
import com.github.reviversmc.modget.library.manager.RepoManager;
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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;

public class V4ManifestCreator implements ManifestCreator {
    private final FabricModPojo modPojo;
    private final ManifestV4 manifestV4;
    private final RepoManager repoManager;
    private final String authToken; //Allow for use of custom token.
    private final OkHttpClient okHttpClient;
    private final ObjectMapper yamlMapper;

    @AssistedInject
    public V4ManifestCreator(
            ManifestV4 manifestV4,
            RepoManager repoManager,
            @Assisted("authToken") String authToken,
            @Assisted("modJarPath") String modJarPath,
            @Named("json") ObjectMapper jsonMapper,
            @Named("yaml") ObjectMapper yamlMapper,
            OkHttpClient okHttpClient
    ) {
        this.manifestV4 = manifestV4;
        this.repoManager = repoManager;
        this.authToken = authToken;
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

        modPojo = tempModPojo;

    }

    @Override
    public Optional<InputStream> createYaml(String modrinthId, String curseforgeId, ModStatus modStatus) {

        return Optional.empty();
    }


    @Override
    public boolean isModPresent() {
        try {
            ManifestRepository mainManifestRepo = repoManager.getRepo(0);

            LookupTable mainRepoTable = mainManifestRepo.getOrDownloadLookupTable();
            List<LookupTableEntry> lookupTableEntries = mainRepoTable.getOrDownloadEntries();

            for (LookupTableEntry entry : lookupTableEntries) {
                if (modPojo.getJson().get("id").equals(entry.getId())) return true;
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
