package io.github.awesomemoder316.modgetcreate.commands;

import dagger.Component;

@Component(modules = {ModgetCreateCommandModule.class, ModgetCreateCommandModule.DefaultCommandNameModule.class})
public interface CommandManagerComponent {
    CommandManager getCommandManager();
}
