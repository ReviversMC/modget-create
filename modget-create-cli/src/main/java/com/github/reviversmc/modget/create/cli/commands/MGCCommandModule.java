package com.github.reviversmc.modget.create.cli.commands;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;


@Module
public interface MGCCommandModule {
        @Binds
        @Singleton
        CommandManager commandManager(MGCCommandManager impl);

        @Binds
        @IntoSet
        Command exitCommand(ExitCommand impl);

        @Binds
        @IntoSet
        Command helpCommand(HelpCommand impl);

        @Binds
        @IntoSet
        Command loginCommand(LoginCommand impl);

        @Binds
        @IntoSet
        Command versionCommand(VersionCommand impl);

        @Module
        class DefaultCommandNameModule {
                @Provides
                @Named("default command name")
                public static String defaultCommandName() {
                        return "help";
                }
        }
}
