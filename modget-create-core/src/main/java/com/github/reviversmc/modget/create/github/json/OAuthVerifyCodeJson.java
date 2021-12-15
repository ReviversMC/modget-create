package com.github.reviversmc.modget.create.github.json;

/**
 * All variables follow lower_underscore_case, as that is the response given by Github.
 */
public class OAuthVerifyCodeJson {

    private final String device_code;
    private final String user_code;
    private final String verification_uri;
    private final int expires_in;
    private final int interval;

    public OAuthVerifyCodeJson(String device_code, String user_code, String verification_uri, int expires_in, int interval) {
        this.device_code = device_code;
        this.user_code = user_code;
        this.verification_uri = verification_uri;
        this.expires_in = expires_in;
        this.interval = interval;
    }

    public String getDeviceCode() {
        return device_code;
    }

    public String getUserCode() {
        return user_code;
    }

    public String getVerificationUri() {
        return verification_uri;
    }

    public int getExpiresIn() {
        return expires_in;
    }

    public int getInterval() {
        return interval;
    }
}
