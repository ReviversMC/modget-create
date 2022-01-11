package com.github.reviversmc.modget.create.manifests;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.library.manager.RepoManager;
import dagger.Module;
import dagger.Provides;

import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

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
            System.out.println(
                    colorize(
                            "Could not connect Manifest Repository! Is your internet or GitHub down?",
                            Attribute.RED_TEXT()
                    )
            );
            System.exit(0);
        }
        return null; //We already exited, this is just here to pacify the compiler.
    }
}
