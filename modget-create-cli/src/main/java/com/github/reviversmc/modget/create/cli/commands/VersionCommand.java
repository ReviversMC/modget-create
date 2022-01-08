package com.github.reviversmc.modget.create.cli.commands;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.io.IOException;
import java.util.*;

import javax.inject.Inject;
import javax.inject.Named;

import com.diogonunes.jcolor.Attribute;

@Named("version command")
public class VersionCommand implements Command {

    @Inject
    public VersionCommand() {

    }

    @Override
    public void onCommand(Map<String, List<String>> args) {
        Properties versionProperties = new Properties();

        try {
            versionProperties.load(getClass().getClassLoader().getResourceAsStream("version.properties"));
            System.out.println(
                    colorize(
                            "modget-create Version: " + versionProperties.getProperty("ModgetCreateVersion") + "\n" +
                                    "Supported modget Manifest Version: " + versionProperties.getProperty("ModgetManifestVersion"),
                            Attribute.GREEN_TEXT()
                    )
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<String> getCommandNames() {
        return List.of("supported", "ver", "version");
    }

    @Override
    public String getDescription() {
        return "This command lists the version of modget-create," +
                " and the manifest version that will be created by it.\n" +
                "There are no parameters for this command.";
    }
}
