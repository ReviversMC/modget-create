package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static com.diogonunes.jcolor.Ansi.colorize;

public class VersionCommand implements IModgetCreateCommand {

    @Override
    public void onCommand(HashMap<String, String> args) {
        Properties versionProperties = new Properties();

        try {
            versionProperties.load(getClass().getClassLoader().getResourceAsStream("version.properties"));
            System.out.println(
                    colorize(
                    "Modget-Create-CE Version: " + versionProperties.getProperty("ModgetCreateVersion") + "\n" +
                            "Supported Modget Manifest Version: " + versionProperties.getProperty("ModgetManifestVersion"),
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
    public void sendHelpMessage() {
        IModgetCreateCommand.super.sendHelpMessage();
    }
}
