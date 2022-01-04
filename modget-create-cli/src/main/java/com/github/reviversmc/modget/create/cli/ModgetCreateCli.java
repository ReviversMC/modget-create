package com.github.reviversmc.modget.create.cli;

import java.util.Scanner;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.cli.commands.CommandManager;
import com.github.reviversmc.modget.create.cli.commands.DaggerCommandManagerComponent;

import javax.inject.Inject;

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

        Scanner scanner = new Scanner(System.in);

        System.out.println(
                colorize(
                        "modget-create successfully started!",
                        Attribute.GREEN_TEXT()
                )
        );
        //noinspection InfiniteLoopStatement, It is not an infinite loop as "Exit" command can be called.
        while (true) {
            System.out.print("modget-create> ");
            commandManager.callCommand(scanner.nextLine());
        }
    }
}
