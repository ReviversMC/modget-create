package com.github.reviversmc.modget.create.cli.commands;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;


@Module
public interface CommandModule {

    @Binds
    CommandManager commandManager(MGCCommandManager mgcCommandManager);

    @Binds
    @IntoSet
    Command createCommand(CreateCommand createCommand);

    @Binds
    @Named("default")
    Command defaultCommand(HelpCommand helpCommand);

    @Binds
    @IntoSet
    Command exitCommand(ExitCommand exitCommand);

    @Binds
    @IntoSet
    Command helpCommand(HelpCommand helpCommand);

    @Binds
    @IntoSet
    Command loginCommand(LoginCommand loginCommand);

    @Binds
    @IntoSet
    Command versionCommand(VersionCommand versionCommand);

}
