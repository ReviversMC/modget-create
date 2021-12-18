package com.github.reviversmc.modget.create.github.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthAccessTokenJson {
    private String accessToken;
    private String tokenType;
    private String scope;

    private String error;
    private String errorDescription;
    private String errorUri;

    public OAuthAccessTokenJson() {
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccess_token(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("token_type")
    public void setToken_type(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonProperty("error_description")
    public String getErrorDescription() {
        return errorDescription;
    }

    @JsonProperty("error_description")
    public void setError_description(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @JsonProperty("error_uri")
    public String getErrorUri() {
        return errorUri;
    }

    @JsonProperty("error_uri")
    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }
}
