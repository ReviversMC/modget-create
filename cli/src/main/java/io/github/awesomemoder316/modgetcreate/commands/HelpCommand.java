package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import javax.inject.Inject;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class HelpCommand implements Command {

    @Inject
    public HelpCommand() {

    }

    @Override
    public void onCommand(List<String> args) {
        //Confirmed that this is a "-i" or "--info" arg, as there are no other accepted args.
        if (!args.isEmpty()) {
            String[] splitArg = args.get(0).split(" ");

            if (splitArg.length > 1) {
                String param = splitArg[1];

                //--help should never be a valid param, but should call the help message for the command.
                //If anyone can find a better way to do this via di, please pr.
                DaggerCommandManagerComponent.create().getCommandManager().callCommand(param + " --help");
                return; //If the call fails, another instance of this#onCommand() will be called anyway.

            }
        }

        System.out.println(
                colorize(
                        "Displaying all available commands:\n" +
                                "\"end\"; \"exit\"; \"stop\"; \"quit\": Shut down modget-create.\n" +
                                "\"help\": This current command.\n" +
                                "\"supported\"; \"ver\"; \"version\": Get the version of modget-create, and the supported manifest version.\n" +
                                "To get info on args/parameters/flags on a command, do \"help -i <command>\".",
                        Attribute.CYAN_TEXT()
                )
        );


    }

    @Override
    public List<String> getCommandNames() {
        return List.of("help");
    }

    @Override
    public String getDescription() {
        return "This command lists the usage of all available commands.\n" +
                "Parameters definitions:\n" +
                "-i <command>, --info <command>: Get more information on a command, as specified in <command>.";
    }

    @Override
    public List<String> getOptionalParameters() {
        return List.of("-i", "--info");
    }
}
