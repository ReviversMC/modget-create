package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import java.util.HashMap;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public interface IModgetCreateCommand {
    /**
     * This function is called when the appropriate command is invoked.
     * @param args A list of arguments (or parameters as it is referred to multiple times in MCCE)
     *             that can be used. Stored in a {@link HashMap}, param: value format.
     */
    void onCommand(HashMap<String, String> args);

    /**
     * Gets all the names/aliases for the command.
     * @return The list of names/aliases.
     */
    List<String> getCommandNames();

    /**
     * Gets all required parameters accepted by the command.
     * @return The list of required parameters.
     */
    default List<String> getRequiredParameters() {
        return List.of();
    }

    /**
     * Gets all the optional parameters accepted by the command.
     * @return The list of optional parameters.
     */
    default List<String> getOptionalParameters() {
        return List.of();
    }

    /**
     * Sends a help message for this command, which informs a users of all available options.
     */
    default void sendHelpMessage() {

        StringBuilder messageBuilder = new StringBuilder("Sending help message...\nCommand names: ");
        for (String commandName: getCommandNames()) {
            messageBuilder.append("\"");
            messageBuilder.append(commandName);
            messageBuilder.append("\"");

            if (!(getCommandNames().get(getCommandNames().size() - 1).equals(commandName))) {
                messageBuilder.append(", ");
            }

        }

        System.out.println(colorize(messageBuilder.toString(), Attribute.CYAN_TEXT()));
    }
}
