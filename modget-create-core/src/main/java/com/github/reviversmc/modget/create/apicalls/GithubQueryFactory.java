package com.github.reviversmc.modget.create.apicalls;

import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface GithubQueryFactory {
    V4GithubQuery create(String authToken);
}
