package com.github.reviversmc.modget.create.cli;

import java.util.Scanner;

import com.github.reviversmc.modget.create.cli.commands.CommandManager;
import com.github.reviversmc.modget.create.cli.commands.CommandManagerComponent;
import com.github.reviversmc.modget.create.cli.commands.DaggerCommandManagerComponent;

public class ModgetCreateCli {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CommandManagerComponent commandManagerComponent = DaggerCommandManagerComponent.create();
        CommandManager commandManager = commandManagerComponent.getCommandManager();

        //noinspection InfiniteLoopStatement, It is not an infinite loop as "Exit" command can be called.
        while (true) {
            System.out.print("modget-create> ");
            commandManager.callCommand(scanner.nextLine());
        }
    }
}
