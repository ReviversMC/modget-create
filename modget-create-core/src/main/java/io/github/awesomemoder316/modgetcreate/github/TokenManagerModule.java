package io.github.awesomemoder316.modgetcreate.github;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface TokenManagerModule {
    @Binds
    @Singleton
    TokenManager githubTokenManager(GithubTokenManager tokenManager);
}
