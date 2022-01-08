package com.github.reviversmc.modget.create.github;

import com.github.reviversmc.modget.create.data.OAuthVerifyCodePojo;

import java.io.IOException;

public interface TokenOAuth {

    /**
     * Creates an OAuthAccessToken, which can be used.
     * This token automatically overwrites any previous value in {@link TokenManager#getToken()}
     *
     * @param oAuthVerifyCodePojo Can be obtained from {@link TokenOAuth#getOAuthVerifyCode()}
     * @return True if successful, false otherwise. The token can then be obtained by {@link TokenManager#getToken()}
     */
    boolean createOAuthAccessToken(OAuthVerifyCodePojo oAuthVerifyCodePojo) throws InterruptedException, IOException;

    /**
     * Gets the verification code, required by {@link TokenOAuth#createOAuthAccessToken(OAuthVerifyCodePojo)}
     * This method does not automatically display the verification code to the user.
     *
     * @return The response by GitHub in POJO.
     */
    OAuthVerifyCodePojo getOAuthVerifyCode();
}
