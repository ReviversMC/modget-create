package com.github.reviversmc.modget.create.manifests;

import dagger.assisted.AssistedFactory;

import javax.inject.Named;

@AssistedFactory
public interface ManifestCreatorFactory {
    V4ManifestCreator create(@Named("authToken") String authToken, @Named("modJarPath") String modJarPath);
}
