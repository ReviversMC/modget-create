package com.github.reviversmc.modget.create.github;


import okhttp3.OkHttpClient;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.okhttp3.OkHttpGitHubConnector;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GithubTokenManager implements TokenManager {

    private final OkHttpClient okHttpClient;
    private String githubToken;

    @Inject
    public GithubTokenManager(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
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
            return GitHubBuilder.fromProperties(authProperties)
                    .withConnector(
                            new OkHttpGitHubConnector(
                                    okHttpClient
                            )
                    ).build()
                    .isCredentialValid();

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
