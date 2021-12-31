package com.github.reviversmc.modget.create.manifests;

import com.github.reviversmc.modget.create.data.ModStatus;

import java.io.InputStream;
import java.util.Optional;

public interface ManifestCreator {
    //TODO(Add javadoc annotations)

    boolean isModPresent();
    Optional<InputStream> createYaml(String modrinthId, String curseforgeId, ModStatus modStatus);
    boolean isUsable();
}
