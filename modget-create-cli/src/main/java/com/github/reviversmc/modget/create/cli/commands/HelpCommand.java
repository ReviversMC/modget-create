package com.github.reviversmc.modget.create.cli.commands;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import com.diogonunes.jcolor.Attribute;

public class HelpCommand implements Command {

    @Inject
    public HelpCommand() {

    }

    @Override
    public void onCommand(Map<String, Optional<String>> args) {
        System.out.println(
                colorize(
                        "Displaying all available commands:\n" +
                                "\"auth\", \"authenticate\", \"gh\", \"git\", \"github\", \"login\", \"token\": Authenticate with Github for automatic PRs.\n" +
                                "\"create\", \"manifest\", \"publish\", \"upload\": Creates a manifest\n" +
                                "\"end\"; \"exit\"; \"stop\"; \"quit\": Shut down modget-create.\n" +
                                "\"help\": This current command.\n" +
                                "\"supported\"; \"ver\"; \"version\": Get the version of modget-create, and the supported manifest version.\n" +
                                "To get info on args/parameters/flags on a command, do \"help -i <command>\".",
                        Attribute.GREEN_TEXT()
                )
        );
    }

    @Override
    public List<String> getCommandNames() {
        return List.of("help");
    }

    @Override
    public String getDescription() {
        return "This command lists the usage of all available commands.\n";
    }
}
