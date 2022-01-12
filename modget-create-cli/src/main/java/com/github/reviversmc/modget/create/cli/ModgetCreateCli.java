package com.github.reviversmc.modget.create.cli;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.cli.commands.CommandManager;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import static com.diogonunes.jcolor.Ansi.colorize;

public class ModgetCreateCli {

    public static void main(String[] args) {

        System.out.println(
                colorize(
                        "Starting modget-create...",
                        Attribute.GREEN_TEXT()
                )
        );

        CLIComponent cliComponent = DaggerCLIComponent.create();
        CommandManager commandManager = cliComponent.getCommandManager();
        LineReader lineReader = cliComponent.getLineReader();

        System.out.println(
                colorize(
                        "modget-create successfully started!\n" +
                                "Don't know how to use modget-create? Enter the command \"?\".",
                        Attribute.GREEN_TEXT()
                )
        );
        //noinspection InfiniteLoopStatement, It is not an infinite loop as "Exit" command can be called.
        while (true) {
            commandManager.callCommand(lineReader.readLine("modget-create> "));
        }
    }
}
