package com.github.reviversmc.modget.create.github.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * All variables follow lower_underscore_case, as that is the response given by Github.
 */
public class OAuthVerifyCodeJson {

    private String deviceCode;
    private String userCode;
    private String verificationUri;
    private int expiresIn;
    private int interval;

    public OAuthVerifyCodeJson() {

    }

    @JsonProperty("device_code")
    public String getDeviceCode() {
        return deviceCode;
    }

    @JsonProperty("device_code")
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @JsonProperty("user_code")
    public String getUserCode() {
        return userCode;
    }

    @JsonProperty("user_code")
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @JsonProperty("verification_uri")
    public String getVerificationUri() {
        return verificationUri;
    }

    @JsonProperty("verification_uri")
    public void setVerificationUri(String verificationUri) {
        this.verificationUri = verificationUri;
    }

    @JsonProperty("expires_in")
    public int getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
