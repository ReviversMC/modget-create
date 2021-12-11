package io.github.awesomemoder316.modgetcreate.commands;

public interface ICommandManager {
    /**
     * Add a single commands to register.
     * @param command A {@link IModgetCreateCommand} to register.
     * @return {@link ICommandManager}, so that chaining registrations is possible.
     */
    ICommandManager addCommand(IModgetCreateCommand command);

    /**
     * Pass a command to an implementation of {@link ICommandManager},
     * and the command will automatically be matched and called.
     * WARNING: If there are duplicate command names, the first match found will be called.
     * @param command The command a user sends.
     */
    void callCommand(String command);

    /**
     * Remove a single command to deregister.
     * @param command A ${@link IModgetCreateCommand} to deregister.
     * @return {@link ICommandManager}, so that chaining registrations is possible.
     */
    ICommandManager removeCommand(IModgetCreateCommand command);

}
