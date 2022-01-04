package com.github.reviversmc.modget.create.combined;

import com.github.reviversmc.modget.create.cli.CLIMiscModule;
import com.github.reviversmc.modget.create.cli.commands.MGCCommandModule;
import com.github.reviversmc.modget.create.dependencies.DependencyModule;
import com.github.reviversmc.modget.create.github.TokenManagerModule;
import com.github.reviversmc.modget.create.main.EntryPoint;
import com.github.reviversmc.modget.create.manifests.ManifestModule;
import dagger.Component;

import javax.inject.Named;
import javax.inject.Singleton;

@Component(modules = {
        CLIMiscModule.class,
        DependencyModule.class,
        EntryPointModule.class,
        ManifestModule.class,
        MGCCommandModule.class,
        MGCCommandModule.DefaultCommandNameModule.class,
        TokenManagerModule.class
})
@Singleton
public interface EntryPointComponent {

    @Named("cli")
    EntryPoint getCliEntryPoint();

    @Named("gui")
    EntryPoint getGuiEntryPoint();
}
