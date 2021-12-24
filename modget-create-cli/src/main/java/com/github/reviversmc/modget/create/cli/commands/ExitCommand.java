package com.github.reviversmc.modget.create.cli.commands;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import com.diogonunes.jcolor.Attribute;

public class ExitCommand implements Command {

    @Inject
    public ExitCommand() {

    }

    @Override
    public void onCommand(Map<String, Optional<String>> args) {
        System.out.println(
                colorize(
                        "modget-create is shutting down...", Attribute.GREEN_TEXT()
                )
        );
        System.exit(0);
    }

    @Override
    public List<String> getCommandNames() {
        return List.of("end", "exit", "stop", "quit");
    }

    @Override
    public String getDescription() {
        return "This command stops modget-create.\n" +
                "Your github token, if entered, will be forgotten by modget-create.\n" +
                "There are no parameters for this command.";
    }

}
