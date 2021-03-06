package com.github.reviversmc.modget.create.manifests;

import com.github.reviversmc.modget.create.data.ModStatus;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;

import java.util.List;

@AssistedFactory
public interface ManifestCreatorFactory {
    V4ManifestCreator create(
            @Assisted("modVersions") List<String> modVersions,
            @Assisted("updateAlternatives") List<String> updateAlternatives,
            ModStatus modStatus,
            @Assisted("authToken") String authToken, //Null value is fine.
            @Assisted("curseId") String curseforgeId,
            @Assisted("modJarPath") String modJarPath,
            @Assisted("modrinthId") String modrinthId,
            @Assisted("wiki") String wiki
    );
}
