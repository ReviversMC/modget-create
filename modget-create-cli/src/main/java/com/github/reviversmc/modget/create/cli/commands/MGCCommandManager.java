package com.github.reviversmc.modget.create.cli.commands;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.util.*;

import javax.inject.Inject;
import javax.inject.Named;

import com.diogonunes.jcolor.Attribute;

public class MGCCommandManager implements CommandManager {
    private final Command defaultCommand; //Being null means that the default command is not specified.
    private final Set<Command> modgetCreateCommands;


    @Inject
    public MGCCommandManager(Set<Command> modgetCreateCommands,
                             @Named("default command name") String defaultCommandName) {
        this.modgetCreateCommands = modgetCreateCommands;

        Command tempDefaultCommand = null;
        for (Command command : modgetCreateCommands) {

            for (String name : command.getCommandNames()) {
                if (name.equals(defaultCommandName)) {
                    tempDefaultCommand = command;
                    break;
                }
            }

            if (tempDefaultCommand != null) break;
        }

        this.defaultCommand = tempDefaultCommand;
    }

    @Override
    public void callCommand(String commandWithParams) {
        /*
        All parameters are in the format "--param value".
        Valid example: "publish -t realToken"
        Valid example: "publish --token realToken"
        Invalid example: "publish -t real token"
         */
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

                //Try to find the appropriate command
                if (commandIssued.equalsIgnoreCase(name)) {

                    for (List<String> reqParamList : command.getRequiredParameters()) {
                        boolean found = false;

                        for (String param : args.keySet()) {

                            if (param.equals("-h") || param.equals("--help")) { //Intercept before error is given.
                                command.sendHelpMessage();
                                return;
                            }

                            if (reqParamList.contains(param) &&
                                    //Protect against required params with no value
                                    args.getOrDefault(param, Optional.empty()).isPresent()) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {

                            System.out.println(
                                    colorize(
                                            "You are missing some required parameters!",
                                            Attribute.RED_TEXT()
                                    )
                            );

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
