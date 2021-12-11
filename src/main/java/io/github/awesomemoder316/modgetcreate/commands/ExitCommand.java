package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class ExitCommand implements IModgetCreateCommand {
    @Override
    public void onCommand(List<String> args) {
        System.out.println(
                colorize(
                        "Modget-Create-CE is shutting down...", Attribute.GREEN_TEXT()
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
        return "This command stops Modget-Create-CE.\n" +
                "Your github token, if entered, will be forgotten by MCCE.\n" +
                "There are no parameters for this command.";
    }

}
