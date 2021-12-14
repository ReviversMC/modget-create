package io.github.awesomemoder316.modgetcreate.commands;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

import javax.inject.Named;


@Module
public interface ModgetCreateCommandModule {
        @Binds
        CommandManager commandManager(MCCommandManager impl);

        @Binds
        @IntoSet
        Command exitCommand(ExitCommand impl);

        @Binds
        @IntoSet
        Command helpCommand(HelpCommand impl);

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
