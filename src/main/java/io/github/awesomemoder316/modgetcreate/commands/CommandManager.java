package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

@Singleton
@Named("command manager")
public class CommandManager implements ICommandManager {
    private final List<IModgetCreateCommand> modgetCreateCommands = new ArrayList<>();
    private final IModgetCreateCommand helpCommand; //Fallback if nothing is found.

    @Inject
    public CommandManager(
            @Named("exit command") IModgetCreateCommand exitCommand,
            @Named("help command") IModgetCreateCommand helpCommand,
            @Named("version command") IModgetCreateCommand versionCommand
    ) {
        this.helpCommand = helpCommand;

        this.addCommand(exitCommand)
                .addCommand(helpCommand)
                .addCommand(versionCommand);
    }

    @Override
    public ICommandManager addCommand(IModgetCreateCommand command) {
        modgetCreateCommands.add(command);
        return this;
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
        String command = splitCmd[0];
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

        for (IModgetCreateCommand modgetCreateCommand: modgetCreateCommands) {

            for (String name: modgetCreateCommand.getCommandNames()) {
                if (command.equalsIgnoreCase(name)) {

                    for (String requiredParam: modgetCreateCommand.getRequiredParameters()) {
                        boolean found = false;

                        for (String param: params) {
                            if (param.split(" ")[0].equals(requiredParam)) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            modgetCreateCommand.sendHelpMessage();
                            return;
                        }
                    }

                    for (String param: params) {
                        boolean recognised = false;

                        for (String reqParam: modgetCreateCommand.getRequiredParameters()) {
                            if (param.split(" ")[0].equals(reqParam)) {
                                recognised = true;
                                break;
                            }
                        }

                        if (recognised) break;

                        for (String optionalParam: modgetCreateCommand.getOptionalParameters()) {
                            if (param.split(" ")[0].equals(optionalParam)) {
                                recognised = true;
                                break;
                            }
                        }

                        if (!recognised) {
                            modgetCreateCommand.sendHelpMessage();
                            return;
                        }
                    }

                    modgetCreateCommand.onCommand(params);
                    return;
                }
            }

        }
        System.out.println(
                colorize(
                        "Invalid command! Showing \"help\" command...",
                        Attribute.RED_TEXT()
                )
        );
        helpCommand.onCommand(List.of());
    }

    @Override
    public ICommandManager removeCommand(IModgetCreateCommand command) {
        modgetCreateCommands.remove(command);
        return this;
    }
}
