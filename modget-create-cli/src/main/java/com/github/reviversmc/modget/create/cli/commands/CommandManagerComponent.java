package com.github.reviversmc.modget.create.cli.commands;

import javax.inject.Singleton;

import com.github.reviversmc.modget.create.dependencies.DependencyModule;
import com.github.reviversmc.modget.create.github.TokenManagerModule;

import dagger.Component;

@Component(modules = {
        DependencyModule.class,
        MGCCommandModule.class,
        MGCCommandModule.DefaultCommandNameModule.class,
        TokenManagerModule.class
})
@Singleton
public interface CommandManagerComponent {
    CommandManager getCommandManager();
}
