package io.github.awesomemoder316.modgetcreate.github.json;

public class OAuthAccessTokenJson {
    private final String access_token;
    private final String token_type;
    private final String scope;

    private final String error;

    public OAuthAccessTokenJson(String access_token, String token_type, String scope, String error) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.scope = scope;
        this.error = error;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public String getError() {
        return error;
    }
}
