package io.github.awesomemoder316.modgetcreate.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements ICommandManager {
    private final List<IModgetCreateCommand> modgetCreateCommands = new ArrayList<>();

    //TODO(Make this a singleton via DI software)
    public CommandManager() {

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
        Valid example: "publish --t realToken"
        Invalid example: "publish --t real token"
         */

        String command = commandWithParams.split(" --")[0];
        HashMap<String, String> parameters = new HashMap<>();

        for (String parameter: commandWithParams.split(" --")) {
            if (command.equals(parameter)) continue;

            //Assume that the user entered the value for the param correctly.
            parameters.put(
                    parameter.split(" ")[0],
                    parameter.split(" ").length > 1 ? //Stop ArrayOutOfBoundsEx.
                    parameter.split(" ")[1] : ""
            );
        }

        for (IModgetCreateCommand modgetCreateCommand: modgetCreateCommands) {

            for (String name: modgetCreateCommand.getCommandNames()) {
                if (command.equalsIgnoreCase(name)) {

                    //Make param check.
                    //Check for required param, and send help message if failed.
                    //Check for optional param, and send help message if param is invalid
                    //Else, call the command.

                    if (!parameters.keySet().containsAll(modgetCreateCommand.getRequiredParameters())) {
                        modgetCreateCommand.sendHelpMessage();
                        return; //Insufficient params.
                    }

                    for (String enteredParams: parameters.keySet()) {
                        if (!enteredParams.equals("") &&
                                !enteredParams.equals(" ") &&
                                !modgetCreateCommand.getRequiredParameters().contains(enteredParams) &&
                                !modgetCreateCommand.getOptionalParameters().contains(enteredParams)) {
                            modgetCreateCommand.sendHelpMessage();
                            return; //Invalid params
                        }
                    }

                    modgetCreateCommand.onCommand(parameters);
                }
            }

        }
    }

    @Override
    public ICommandManager removeCommand(IModgetCreateCommand command) {
        modgetCreateCommands.remove(command);
        return this;
    }
}
