package com.github.reviversmc.modget.create.github;


import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.connector.GitHubConnector;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GithubTokenManager implements TokenManager {

    private final GitHubConnector gitHubConnector;
    private String githubToken;

    @Inject
    public GithubTokenManager(GitHubConnector gitHubConnector) {
        this.gitHubConnector = gitHubConnector;
    }

    @Override
    public Optional<String> getToken() {
        if (githubToken != null) return Optional.of(githubToken);
        return Optional.empty();
    }

    @Override
    public boolean setToken(String token) {
        if (validateToken(token)) {
            this.githubToken = token;
            return true;
        }
        return false;
    }

    @Override
    public boolean validateToken(String token) {
        //We unfortunately cannot check the scope of the token with the api.
        Properties authProperties = new Properties();
        authProperties.setProperty("oauth", token);

        try {
            GitHub githubAPI =  GitHubBuilder.fromProperties(authProperties)
                    .withConnector(gitHubConnector).build();

            return githubAPI.isCredentialValid() && githubAPI.getRateLimit().getRemaining() > 0;

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
