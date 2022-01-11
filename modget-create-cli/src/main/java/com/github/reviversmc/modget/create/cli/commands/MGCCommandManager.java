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
                             @Named("default") Command defaultCommand
    ) {
        this.modgetCreateCommands = modgetCreateCommands;
        this.defaultCommand = defaultCommand;
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
        HashMap<String, List<String>> args = new HashMap<>();

        for (int x = 1; x < splitCmd.length; x++) {

            if (splitCmd[x].startsWith("-") || splitCmd[x].startsWith("--")) {

                List<String> list = args.getOrDefault(splitCmd[x], new ArrayList<>());

                if (x + 1 < splitCmd.length &&
                        !splitCmd[x + 1].startsWith("-") && !splitCmd[x + 1].startsWith("--")) {
                    list.add(splitCmd[x + 1]);
                }

                args.put(splitCmd[x], list);
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

                            if (reqParamList.contains(param)) {
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
