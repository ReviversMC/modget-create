package com.github.reviversmc.modget.create.cli.commands;

import javax.inject.Singleton;

import com.github.reviversmc.modget.create.cli.CLIMiscModule;
import com.github.reviversmc.modget.create.dependencies.DependencyModule;
import com.github.reviversmc.modget.create.github.TokenManagerModule;

import com.github.reviversmc.modget.create.manifests.ManifestModule;
import dagger.Component;

@Component(modules = {
        CLIMiscModule.class,
        DependencyModule.class,
        ManifestModule.class,
        CommandModule.class,
        TokenManagerModule.class
})
@Singleton
public interface CommandManagerComponent {
    CommandManager getCommandManager();
}