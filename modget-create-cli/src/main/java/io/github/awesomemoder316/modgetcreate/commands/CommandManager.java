package io.github.awesomemoder316.modgetcreate.commands;

/**
 * There is no method to add commands,
 * as commands should be passed through the constructor of an implementation.
 */
public interface CommandManager {
    /**
     * Pass a command to an implementation of {@link CommandManager},
     * and the command will automatically be matched and called.
     * WARNING: If there are duplicate command names, the first match found will be called.
     * If no commands are found, the help command will be sent.
     * @param command The command a user sends.
     */
    void callCommand(String command);
}
