package com.github.reviversmc.modget.create.manifests;
import java.io.OutputStream;
import java.util.Optional;

public interface ManifestCreator {

    /**
     * Checks if the mod is already present.
     * @return True if the mod is present, false otherwise. False will be given for errors as well.
     */
    boolean isModPresent();

    /**
     * Creates the main.yml for a mod.
     * @return An {@link Optional<OutputStream>} of the yaml if successful, {@link Optional#empty()} otherwise.
     */
    Optional<String> createMainYaml();

    /**
     * Checks if the mod to create the manifest for is valid.
     * @return True if it is a valid Fabric mod, false otherwise.
     */
    boolean isUsable();
}
