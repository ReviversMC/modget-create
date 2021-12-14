package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import javax.inject.Inject;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class ExitCommand implements Command {

    @Inject
    public ExitCommand() {

    }

    @Override
    public void onCommand(List<String> args) {
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
