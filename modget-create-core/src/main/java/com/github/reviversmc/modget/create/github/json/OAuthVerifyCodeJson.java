package com.github.reviversmc.modget.create.github.json;

/**
 * All variables follow lower_underscore_case, as that is the response given by Github.
 */
public class OAuthVerifyCodeJson {

    private String device_code;
    private String user_code;
    private String verification_uri;
    private int expires_in;
    private int interval;

    public OAuthVerifyCodeJson() {

    }

    public String getDeviceCode() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getUserCode() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getVerificationUri() {
        return verification_uri;
    }

    public void setVerification_uri(String verification_uri) {
        this.verification_uri = verification_uri;
    }

    public int getExpiresIn() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
