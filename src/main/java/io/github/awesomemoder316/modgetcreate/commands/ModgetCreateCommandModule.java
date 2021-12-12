package io.github.awesomemoder316.modgetcreate.commands;

import dagger.Binds;
import dagger.Module;

import javax.inject.Named;

@Module
public interface ModgetCreateCommandModule {

        @Binds @Named("command manager")
        ICommandManager bindCommandManager (CommandManager commandManager);

        @Binds @Named("exit command")
        IModgetCreateCommand bindExitCommand (ExitCommand impl);

        @Binds @Named("help command")
        IModgetCreateCommand bindHelpCommand (HelpCommand impl);

        @Binds @Named("version command")
        IModgetCreateCommand bindVersionCommand (VersionCommand impl);

}
