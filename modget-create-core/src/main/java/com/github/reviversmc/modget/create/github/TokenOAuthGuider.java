package com.github.reviversmc.modget.create.github;

public interface TokenOAuthGuider {

    /**
     * Guides a user to create an oAuth token.
     * @return True if a token was successfully created, false otherwise.
     */
    boolean guide();
}
