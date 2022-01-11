package com.github.reviversmc.modget.create.manifests;

import java.io.OutputStream;
import java.util.Optional;

public interface ManifestCreator {

    /**
     * Adds the mod to the lookup table.
     * Equivalent to ManifestCreator#createLookupTable(false)
     *
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     */
    Optional<String> createLookupTable();

    /**
     * Adds the mod to the lookup table.
     *
     * @param forceCreate Force the creation of a yaml, even if one already exists.
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     */
    Optional<String> createLookupTable(boolean forceCreate);

    /**
     * Creates the main.yml for a mod.
     * Equivalent to ManifestCreator#createMainYaml(false)
     *
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     */
    Optional<String> createMainYaml();

    /**
     * Creates the main.yml for a mod.
     *
     * @param forceCreate Force the creation of a yaml, even if one already exists.
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     */
    Optional<String> createMainYaml(boolean forceCreate);

    /**
     * Checks if the mod is already present.
     *
     * @return True if the mod is present, false otherwise. False will be given for errors as well.
     */
    boolean isModPresent();

    /**
     * Checks if all values provided for initialization are valid.
     *
     * @return True if it is a initialization was successful, false otherwise.
     */
    boolean isUsable();
}
