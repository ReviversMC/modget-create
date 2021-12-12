package io.github.awesomemoder316.modgetcreate.commands;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ModgetCreateCommandModule.class)
public interface CommandManagerComponent {
    CommandManager getCommandManager();
}
