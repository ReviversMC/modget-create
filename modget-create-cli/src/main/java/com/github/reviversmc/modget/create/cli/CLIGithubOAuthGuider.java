package com.github.reviversmc.modget.create.cli;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.github.TokenOAuth;
import com.github.reviversmc.modget.create.github.TokenOAuthGuider;
import com.github.reviversmc.modget.create.data.OAuthVerifyCodePojo;

import javax.inject.Inject;

import java.io.IOException;

import static com.diogonunes.jcolor.Ansi.colorize;

public class CLIGithubOAuthGuider implements TokenOAuthGuider {
    private final TokenOAuth tokenOAuth;

    @Inject
    public CLIGithubOAuthGuider(TokenOAuth tokenOAuth) {
        this.tokenOAuth = tokenOAuth;
    }

    @Override
    public boolean guide() {
        try {
            OAuthVerifyCodePojo verifyCodePojo = tokenOAuth.getOAuthVerifyCode();

            if (verifyCodePojo.getError() != null) {
                System.out.println(
                        colorize(
                                "An error occurred when trying to get a verification code. " +
                                        "Maybe try checking your internet?",
                                Attribute.RED_TEXT()
                        )
                );
                return false;
            }

            System.out.println(
                    colorize(
                            "To continue your login, please go to ",
                            Attribute.GREEN_TEXT()
                    ) + colorize(
                            verifyCodePojo.getVerificationUri(),
                            Attribute.BOLD()
                    ) + colorize(
                            " and enter the code ",
                            Attribute.GREEN_TEXT()
                    ) + colorize(
                            verifyCodePojo.getUserCode(),
                            Attribute.BOLD()
                    )
            );

            if (tokenOAuth.createOAuthAccessToken(verifyCodePojo)) {
                System.out.println(
                        colorize(
                                "Authentication success!",
                                Attribute.GREEN_TEXT()
                        )
                );

                return true;
            } else {
                System.out.println(
                        colorize(
                                "Authentication failed! Maybe check your internet connection and retry?"
                        )
                );
            }

        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
