package com.github.reviversmc.modget.create.manifests;

import com.therandomlabs.curseapi.CurseException;

import java.io.IOException;
import java.util.Optional;

public interface ManifestCreator {

    /**
     * Adds the mod to the lookup table.
     * Equivalent to ManifestCreator#createLookupTable(false)
     *
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     * @throws CurseException If the api calls to Curseforge fails.
     * @throws IOException    If the api calls to GitHub fails.
     */
    Optional<String> createLookupTable() throws CurseException, IOException;

    /**
     * Adds the mod to the lookup table.
     *
     * @param forceCreate Force the creation of a yaml, even if one already exists.
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     * @throws CurseException If the api calls to Curseforge fails.
     * @throws IOException    If the api calls to GitHub fails.
     */
    Optional<String> createLookupTable(boolean forceCreate) throws CurseException, IOException;

    /**
     * Creates the main.yml for a mod.
     * Equivalent to ManifestCreator#createMainYaml(false)
     *
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     * @throws IOException If various web calls fail.
     */
    Optional<String> createMainYaml() throws Exception;

    /**
     * Creates the main.yml for a mod.
     *
     * @param forceCreate Force the creation of a yaml, even if one already exists.
     * @return The yaml in a {@link Optional<String>} if successful, or a {@link Optional#empty()} otherwise.
     * @throws Exception If various web calls fail.
     */
    Optional<String> createMainYaml(boolean forceCreate) throws Exception;

    /**
     * Checks if the mod is already present.
     *
     * @return True if the mod is present, false otherwise. False will be given for errors as well.
     * @throws Exception If the call to retrieve the mod repo fails.
     */
    boolean isModPresent() throws Exception;

    /**
     * Checks if all values provided for initialization are valid.
     *
     * @return True if it is a initialization was successful, false otherwise.
     */
    boolean isUsable();
}
