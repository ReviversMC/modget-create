package io.github.awesomemoder316.modgetcreate.commands;

import dagger.Component;

@Component(modules = {MGCCommandModule.class, MGCCommandModule.DefaultCommandNameModule.class})
public interface CommandManagerComponent {
    CommandManager getCommandManager();
}
