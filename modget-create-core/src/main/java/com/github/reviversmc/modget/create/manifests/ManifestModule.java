package com.github.reviversmc.modget.create.manifests;

import com.github.reviversmc.modget.library.manager.RepoManager;
import dagger.Module;
import dagger.Provides;

import java.util.List;

@Module
public interface ManifestModule {

    @Provides
    static RepoManager repoManager() {
        try {
            RepoManager repoManager = new RepoManager();
            repoManager.init(
                    List.of(
                            "https://raw.githubusercontent.com/ReviversMC/modget-manifests"
                    )
            );
            return repoManager;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
