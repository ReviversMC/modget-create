package com.github.reviversmc.modget.create.cli.commands;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.util.*;

import javax.inject.Inject;
import javax.inject.Named;

import com.diogonunes.jcolor.Attribute;

public class MGCCommandManager implements CommandManager {
    private final Set<Command> modgetCreateCommands;
    private final String defaultCommandName;

    @Inject
    public MGCCommandManager(Set<Command> modgetCreateCommands,
                             @Named("default command name") String defaultCommandName) {
        this.modgetCreateCommands = modgetCreateCommands;
        this.defaultCommandName = defaultCommandName;
    }

    @Override
    public void callCommand(String commandWithParams) {
        /*
        All parameters are in the format "--param value".
        Valid example: "publish -t realToken"
        Valid example: "publish --token realToken"
        Invalid example: "publish -t real token"
         */
        Command defaultCommand = null;
        String[] splitCmd = commandWithParams.split(" ");
        String commandIssued = splitCmd[0];
        HashMap<String, Optional<String>> args = new HashMap<>();

        for (int x = 1; x < splitCmd.length; x++) {

            if (splitCmd[x].startsWith("-") || splitCmd[x].startsWith("--")) {
                if (x + 1 < splitCmd.length &&
                        !splitCmd[x + 1].startsWith("-") && !splitCmd[x + 1].startsWith("--")) {
                    args.put(splitCmd[x], Optional.of(splitCmd[x + 1]));
                } else {
                    args.put(splitCmd[x], Optional.empty());
                }
            }
        }

        for (Command command : modgetCreateCommands) {

            for (String name : command.getCommandNames()) {
                if (name.equals(defaultCommandName)) defaultCommand = command;

                if (commandIssued.equalsIgnoreCase(name)) {

                    for (List<String> reqParamList : command.getRequiredParameters()) {
                        boolean found = false;

                        for (String param : args.keySet()) {
                            if (reqParamList.contains(param)) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            command.sendHelpMessage();
                            return;
                        }
                    }


                    for (String param : args.keySet()) {
                        boolean recognised = false;

                        for (List<String> reqParamList : command.getRequiredParameters()) {
                            if (reqParamList.contains(param)) {
                                recognised = true;
                                break;
                            }
                        }

                        if (recognised) break;

                        for (String optionalParam : command.getOptionalParameters()) {
                            if (param.split(" ")[0].equals(optionalParam)) {
                                recognised = true;
                                break;
                            }
                        }

                        if (!recognised) {
                            command.sendHelpMessage();
                            return;
                        }
                    }

                    command.onCommand(args);
                    return;
                }
            }

        }
        System.out.println(
                colorize(
                        "Invalid command! Showing fallback command...",
                        Attribute.RED_TEXT()
                )
        );

        if (defaultCommand == null) {
            System.out.println(
                    colorize(
                            "No fallback command found.",
                            Attribute.RED_TEXT()
                    )
            );
        } else defaultCommand.onCommand(Map.of());
    }
}
