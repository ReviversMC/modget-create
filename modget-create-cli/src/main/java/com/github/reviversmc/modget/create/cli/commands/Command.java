package com.github.reviversmc.modget.create.cli.commands;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.util.List;
import java.util.Map;

import com.diogonunes.jcolor.Attribute;

public interface Command {

    /**
     * This function is called when the appropriate command is invoked.
     *
     * @param args A list of arguments (or parameters as it is referred to multiple times in modget-create)
     *             that can be used.
     */
    void onCommand(Map<String, List<String>> args);

    /**
     * Gets all the names/aliases for the command.
     *
     * @return The list of names/aliases.
     */
    List<String> getCommandNames();

    /**
     * Gets the description for the command, and all its parameters.
     *
     * @return The description for the command, in a {@link String}
     */
    String getDescription();

    /**
     * Gets all required parameters accepted by the command.
     *
     * @return A {@link List}. which contains another {@link List} inside of it.
     * At least one of the parameters inside the inner list has to be given when using this command.
     */
    default List<List<String>> getRequiredParameters() {
        return List.of();
    }

    /**
     * Gets all the optional parameters accepted by the command.
     * There is no "help" parameter, as all invalid parameters send the help message anyway.
     *
     * @return The list of optional parameters.
     */
    default List<String> getOptionalParameters() {
        return List.of();
    }

    /**
     * Sends a help message for this command, which informs users of all available options.
     */
    default void sendHelpMessage() {

        StringBuilder messageBuilder = new StringBuilder("Sending help message...\nCommand names: ");
        getValuesOf(getCommandNames(), messageBuilder, "; ");

        messageBuilder.append(".\n")
                .append("Required parameters: ");

        if (getRequiredParameters().isEmpty()) messageBuilder.append("None");
        else {
            for (List<String> reqParamList : getRequiredParameters()) {
                getValuesOf(reqParamList, messageBuilder, ", or ");
                if (!getRequiredParameters().get(getRequiredParameters().size() - 1).equals(reqParamList)) {
                    messageBuilder.append("; ");
                }
            }
        }

        messageBuilder.append(".\n")
                .append("Optional parameters: ");

        if (getOptionalParameters().isEmpty()) messageBuilder.append("None");
        else getValuesOf(getOptionalParameters(), messageBuilder, "; ");

        messageBuilder.append(".\n\n")
                .append(getDescription());

        System.out.println(colorize(messageBuilder.toString(), Attribute.GREEN_TEXT()));
    }

    /**
     * Does the repetitive work of {@link Command#sendHelpMessage()}
     *
     * @param values   The values to extract.
     * @param toModify The StringBuilder that can be added onto.
     * @param splitter A string to split 2 values from each other.
     */
    private void getValuesOf(List<String> values, StringBuilder toModify, String splitter) {
        for (String value : values) {
            toModify.append("\"")
                    .append(value)
                    .append("\"");

            if (!values.get(values.size() - 1).equals(value)) toModify.append(splitter);

        }
    }
}
