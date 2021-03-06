package com.github.reviversmc.modget.create.github;

import java.io.IOException;
import java.util.Optional;

public interface TokenManager {
    /**
     * Gets the GitHub token of a user, if present.
     *
     * @return An {@link Optional} with the GitHub token, or null.
     */
    Optional<String> getToken();

    /**
     * Sets the GitHub token of a user. Validates the token before setting it.
     *
     * @param token The token to store in memory.
     * @return False if token is invalid or contains insufficient perms, true otherwise.
     * @throws IOException If the api call to validate the token fails.
     */
    boolean setToken(String token) throws IOException;

    /**
     * Validates if a token has the appropriate scopes.
     *
     * @param token The token to authenticate.
     * @return True if it has the correct scopes, false otherwise.
     * @throws IOException If the api call to GitHub fails.
     */
    boolean validateToken(String token) throws IOException;
}
