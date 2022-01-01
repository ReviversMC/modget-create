package com.github.reviversmc.modget.create.manifests;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;

import java.util.List;

@AssistedFactory
public interface ManifestCreatorFactory {
    V4ManifestCreator create(
            List<String> updateAlternatives,
            @Assisted("authToken") String authToken,
            @Assisted("curseId") String curseforgeId,
            @Assisted("modJarPath") String modJarPath,
            @Assisted("modrinthId") String modrinthId
    );
}
