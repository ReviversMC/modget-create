package com.github.reviversmc.modget.create.cli.commands;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.github.TokenManager;

import static com.diogonunes.jcolor.Ansi.colorize;

@Named("login command")
public class LoginCommand implements Command {
    TokenManager tokenManager;

    @Inject
    public LoginCommand(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }


    @Override
    public void onCommand(List<String> args) {

        if (args.isEmpty()) {
            tokenManager.oAuth();
            return;
        }
        String token = "null";

        for (String arg: args) {
            if (arg.startsWith("-t ") || arg.startsWith("--token ")) {
                token = arg.split(" ")[0];
            }
        }

        if (tokenManager.setToken(token)) {
            System.out.println(
                    colorize(
                            "Login success!",
                            Attribute.GREEN_TEXT()
                    )
            );
        } else {
            System.out.println(
                    colorize(
                            "Login failed! Perhaps you had incorrect scopes or an invalid token?",
                            Attribute.RED_TEXT()
                    )
            );
        }

    }

    @Override
    public List<String> getCommandNames() {
        return List.of("gh", "git", "github", "login", "token");
    }

    @Override
    public String getDescription() {
        return "This command allows a user to authenticate with GitHub," +
                " which is needed for automatic PRs of manifests.\n" +
                "Parameters definitions:\n" +
                "-t <token>, --token <token>: Authenticate with a Personal Access Token, which is forgotten on shutdown.";
    }

    @Override
    public List<String> getOptionalParameters() {
        return List.of("-t", "--token");
    }
}
