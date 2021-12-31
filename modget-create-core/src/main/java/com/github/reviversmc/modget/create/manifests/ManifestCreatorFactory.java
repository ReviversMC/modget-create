package com.github.reviversmc.modget.create.manifests;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface ManifestCreatorFactory {
    V4ManifestCreator create(@Assisted("authToken") String authToken,
                             @Assisted("modJarPath") String modJarPath);
}
