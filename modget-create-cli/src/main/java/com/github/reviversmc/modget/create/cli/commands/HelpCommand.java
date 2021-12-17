package com.github.reviversmc.modget.create.cli.commands;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.util.List;

import javax.inject.Inject;

import com.diogonunes.jcolor.Attribute;

public class HelpCommand implements Command {

    @Inject
    public HelpCommand() {

    }

    @Override
    public void onCommand(List<String> args) {

        if (!args.isEmpty()) {

            for (String arg : args) {
                if (arg.startsWith("-i ") || arg.startsWith("--info ")) {
                    //--help should never be a valid param, but should call the help message for the command.
                    //If anyone can find a better way to do this via di, please pr.
                    DaggerCommandManagerComponent.create().getCommandManager().callCommand(
                            arg.split(" ")[1] + " --help"
                    );
                    return; //If the call fails, another instance of this#onCommand() will be called anyway.

                }
            }
        }

        System.out.println(
                colorize(
                        "Displaying all available commands:\n" +
                                "\"auth\", \"authenticate\", \"gh\", \"git\", \"github\", \"login\", \"token\": Authenticate with Github for automatic PRs." +
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
