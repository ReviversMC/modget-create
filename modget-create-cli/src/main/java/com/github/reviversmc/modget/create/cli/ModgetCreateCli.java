package com.github.reviversmc.modget.create.cli;

import java.io.IOException;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.cli.commands.CommandManager;
import com.github.reviversmc.modget.create.cli.commands.DaggerCommandManagerComponent;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import static com.diogonunes.jcolor.Ansi.colorize;

public class ModgetCreateCli {

    public static void main(String[] args) {

        System.out.println(
                colorize(
                        "Starting modget-create...",
                        Attribute.GREEN_TEXT()
                )
        );

        CommandManager commandManager = DaggerCommandManagerComponent.create().getCommandManager();

        Terminal terminal;

        try {
            terminal = TerminalBuilder.terminal();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        LineReader lineReader = LineReaderBuilder.builder()
                .appName("Modget Create CLI")
                .terminal(terminal)
                .build();

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
