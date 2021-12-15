package io.github.awesomemoder316.modgetcreate;

import io.github.awesomemoder316.modgetcreate.commands.CommandManager;
import io.github.awesomemoder316.modgetcreate.commands.CommandManagerComponent;
import io.github.awesomemoder316.modgetcreate.commands.DaggerCommandManagerComponent;

import java.util.Scanner;

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
