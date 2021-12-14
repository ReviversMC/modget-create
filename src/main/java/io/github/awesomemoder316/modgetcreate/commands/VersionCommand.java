package io.github.awesomemoder316.modgetcreate.commands;

import com.diogonunes.jcolor.Attribute;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static com.diogonunes.jcolor.Ansi.colorize;

@Named("version command")
public class VersionCommand implements Command {

    @Inject
    public VersionCommand() {

    }

    @Override
    public void onCommand(List<String> args) {
        Properties versionProperties = new Properties();

        try {
            versionProperties.load(getClass().getClassLoader().getResourceAsStream("version.properties"));
            System.out.println(
                    colorize(
                    "Modget-Create Version: " + versionProperties.getProperty("ModgetCreateVersion") + "\n" +
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
    public String getDescription() {
        return "This command lists the version of Modget-Create," +
                " and the manifest version that will be created by it.\n" +
                "There are no parameters for this command.";
    }
}
