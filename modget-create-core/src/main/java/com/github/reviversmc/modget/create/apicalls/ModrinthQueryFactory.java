package com.github.reviversmc.modget.create.apicalls;

import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface ModrinthQueryFactory {
    ModrinthV1Query create(String projectId);
}
