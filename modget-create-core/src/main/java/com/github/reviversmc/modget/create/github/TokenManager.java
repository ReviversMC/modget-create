package com.github.reviversmc.modget.create.github;

import java.util.Optional;

public interface TokenManager {
    /**
     * Gets the GitHub token of a user, if present.
     * @return An ${@link Optional} with the GitHub token, or null.
     */
    Optional<String> getToken();

    /**
     * Send the user to an o-auth page, and gets them to verify.
     * @return True if successful, false otherwise.
     */
    boolean oAuth();

    /**
     * Sets the GitHub token of a user.
     * @param token The token to store in memory.
     * @return False if token is invalid or contains insufficient perms, true otherwise.
     */
    boolean setToken(String token);

}
