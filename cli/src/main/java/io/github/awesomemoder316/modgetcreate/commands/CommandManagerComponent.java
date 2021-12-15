package io.github.awesomemoder316.modgetcreate.commands;

import dagger.Component;
import io.github.awesomemoder316.modgetcreate.dependencies.DependencyModule;
import io.github.awesomemoder316.modgetcreate.github.TokenManagerModule;

import javax.inject.Singleton;

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
