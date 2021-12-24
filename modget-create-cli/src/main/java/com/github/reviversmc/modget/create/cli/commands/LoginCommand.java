package com.github.reviversmc.modget.create.cli.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.github.TokenManager;
import com.github.reviversmc.modget.create.github.TokenOAuthGuider;

import static com.diogonunes.jcolor.Ansi.colorize;

@Named("login command")
public class LoginCommand implements Command {
    TokenManager tokenManager;
    TokenOAuthGuider tokenOAuthGuider;

    @Inject
    public LoginCommand(TokenManager tokenManager, TokenOAuthGuider tokenOAuthGuider) {
        this.tokenManager = tokenManager;
        this.tokenOAuthGuider = tokenOAuthGuider;
    }


    @Override
    public void onCommand(Map<String, Optional<String>> args) {

        if (args.isEmpty()) {
            tokenOAuthGuider.guide();
            return;
        }

        Optional<String> optionalToken = ArgObtainer.obtain(args, List.of("-t", "--token"));

        if (optionalToken.isEmpty()) { //Should never happen.
            System.out.println(
                    colorize(
                            "An unexpected error occurred!",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }

        if (tokenManager.setToken(optionalToken.get())) {
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
        return List.of("auth", "authenticate", "gh", "git", "github", "login", "token");
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
