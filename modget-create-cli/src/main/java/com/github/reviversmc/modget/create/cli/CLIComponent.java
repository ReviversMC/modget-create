package com.github.reviversmc.modget.create.cli;

import javax.inject.Singleton;

import com.github.reviversmc.modget.create.cli.commands.CommandManager;
import com.github.reviversmc.modget.create.cli.commands.CommandModule;
import com.github.reviversmc.modget.create.dependencies.DependencyModule;
import com.github.reviversmc.modget.create.github.TokenManagerModule;

import com.github.reviversmc.modget.create.manifests.ManifestModule;
import dagger.Component;
import org.jline.reader.LineReader;

@Component(modules = {
        CLIMiscModule.class,
        DependencyModule.class,
        ManifestModule.class,
        CommandModule.class,
        TokenManagerModule.class
})
@Singleton
public interface CLIComponent {
    CommandManager getCommandManager();
    LineReader getLineReader();
}