package com.github.reviversmc.modget.create.github;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface TokenManagerModule {
    @Binds
    @Singleton
    TokenManager githubTokenManager(GithubTokenManager tokenManager);

    @Binds
    TokenOAuth githubOAuth(GithubOAuth githubOAuth);
}
