package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.diogonunes.jcolor.Ansi.colorize;

public class MCCECommandManager implements CommandManager {
    private final Set<Command> modgetCreateCECommands;
    private final String defaultCommandName;

    @Inject
    public MCCECommandManager(Set<Command> modgetCreateCECommands,
                              @Named("default command name") String defaultCommandName) {
        this.modgetCreateCECommands = modgetCreateCECommands;
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
        List<String> params = new ArrayList<>();

        for (int x = 1; x < splitCmd.length; x++) {

            if (splitCmd[x].startsWith("-") || splitCmd[x].startsWith("--")) {
                if (x + 1 < splitCmd.length &&
                        !splitCmd[x + 1].startsWith("-") && !splitCmd[x + 1].startsWith("--")) {
                    params.add(splitCmd[x] + " " + splitCmd[x + 1]);
                } else {
                    params.add(splitCmd[x]);
                }
            }
        }

        for (Command command : modgetCreateCECommands) {

            for (String name : command.getCommandNames()) {
                if (name.equals(defaultCommandName)) defaultCommand = command;

                if (commandIssued.equalsIgnoreCase(name)) {

                    for (List<String> reqParamList : command.getRequiredParameters()) {
                        boolean found = false;

                        for (String param : params) {
                            if (reqParamList.contains(param.split(" ")[0])) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            command.sendHelpMessage();
                            return;
                        }
                    }


                    for (String param : params) {
                        boolean recognised = false;

                        for (List<String> reqParamList : command.getRequiredParameters()) {
                            if (reqParamList.contains(param.split(" ")[0])) {
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

                    command.onCommand(params);
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
        } else defaultCommand.onCommand(List.of());
    }
}
