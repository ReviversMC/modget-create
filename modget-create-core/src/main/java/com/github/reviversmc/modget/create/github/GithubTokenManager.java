package com.github.reviversmc.modget.create.github;


import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.reviversmc.modget.create.apicalls.GithubQueryFactory;

@Singleton
public class GithubTokenManager implements TokenManager {

    private final GithubQueryFactory githubQueryFactory;
    private String githubToken;

    @Inject
    public GithubTokenManager(GithubQueryFactory githubQueryFactory) {
        this.githubQueryFactory = githubQueryFactory;
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


        String scopes = githubQueryFactory.create(token).getScopes();
        if (scopes == null) return false;

        for (String scope : scopes.split(", ")) {
            if (scope.equals("public_repo")) return true;
        }
        return false;
    }
}
