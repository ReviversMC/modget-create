package com.github.reviversmc.modget.create.apicalls;

public interface GithubQuery {
    /**
     * Gets the authorization for a Github token.
     * @return A {@link String} of scopes for the GitHub token, with each scope seperated by a comma.
     */
    String getScopes();

}
