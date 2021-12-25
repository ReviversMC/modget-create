package com.github.reviversmc.modget.create.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.create.github.json.OAuthAccessTokenPojo;
import com.github.reviversmc.modget.create.github.json.OAuthVerifyCodePojo;
import okhttp3.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Objects;

public class GithubOAuth implements TokenOAuth {
    private final ObjectMapper jsonMapper;
    private final OkHttpClient okHttpClient;
    private final String clientId = "3cd493499ff6e8c086c9";
    private final TokenManager tokenManager;

    @Inject
    public GithubOAuth(
            @Named("json") ObjectMapper jsonMapper,
            OkHttpClient okHttpClient,
            TokenManager tokenManager
    ) {
        this.jsonMapper = jsonMapper;
        this.okHttpClient = okHttpClient;
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean createOAuthAccessToken(OAuthVerifyCodePojo oAuthVerifyCodePojo) {
        //String oAuthDeviceId = oAuthVerifyCodeJson.getDeviceCode();

        RequestBody accessTokenRequestBody = new FormBody.Builder()
                .add("client_id", clientId)
                .add("device_code", oAuthVerifyCodePojo.getDeviceCode())
                .add("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
                .build();

        Request accessTokenRequest = new Request.Builder()
                .addHeader("Accept", "application/json")
                .post(accessTokenRequestBody)
                .url("https://github.com/login/oauth/access_token")
                .build();

        try {
            while (true) {
                //noinspection BusyWait, expected behaviour
                Thread.sleep(oAuthVerifyCodePojo.getInterval() * 1000L + 1000L);

                Response accessTokenResponse = okHttpClient.newCall(accessTokenRequest).execute();

                OAuthAccessTokenPojo accessTokenPojo = jsonMapper.readValue(
                        Objects.requireNonNull(accessTokenResponse.body()).string(),
                        OAuthAccessTokenPojo.class
                );

                if (accessTokenPojo.getError() != null) {

                    switch (accessTokenPojo.getError()) {
                        case "slow_down":
                            //noinspection BusyWait, expected behaviour.
                            Thread.sleep(5500L);
                        case "expired_token":
                            //Not checking for unsupported_grant_type.
                        case "access_denied":
                            return false;

                        default:
                            continue;
                    }
                }
                //Should always return true, unless the user's internet suddenly cuts or something unexpected happens.
                return tokenManager.setToken(accessTokenPojo.getAccessToken());
            }
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public OAuthVerifyCodePojo getOAuthVerifyCode() {
        RequestBody verifyCodeRequestBody = new FormBody.Builder()
                .add("client_id", clientId)
                .add("scope", "read:user, public_repo")
                .build();

        Request verifyCodeRequest = new Request.Builder()
                .addHeader("Accept", "application/json")
                .post(verifyCodeRequestBody)
                .url("https://github.com/login/device/code")
                .build();

        try {
            Response verifyCodeResponse = okHttpClient.newCall(verifyCodeRequest).execute();


            return jsonMapper.readValue(
                    Objects.requireNonNull(verifyCodeResponse.body()).string(),
                    OAuthVerifyCodePojo.class
            );
        } catch (IOException ex) {
            ex.printStackTrace();
            OAuthVerifyCodePojo fakeVerifyCodePojo = new OAuthVerifyCodePojo();
            fakeVerifyCodePojo.setError(ex.getMessage());
            return fakeVerifyCodePojo;
        }
    }
}
