package com.github.reviversmc.modget.create.apicalls;

import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface GithubQueryFactory {
    GithubV4Query create(String authToken);
}
