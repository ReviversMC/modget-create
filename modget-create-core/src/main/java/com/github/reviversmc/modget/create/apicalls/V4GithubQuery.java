package com.github.reviversmc.modget.create.apicalls;

import dagger.assisted.Assisted;
import okhttp3.*;

import javax.inject.Inject;
import java.io.IOException;

public class V4GithubQuery implements GithubQuery {

    private final OkHttpClient okHttpClient;
    private final String authToken;

    @Inject
    public V4GithubQuery(OkHttpClient okHttpClient, @Assisted String authToken) {
        this.okHttpClient = okHttpClient;
        this.authToken = authToken;
    }

    @Override
    public String getScopes() {
        RequestBody requestBody = RequestBody.create(
                "" + //Exists solely for readability.
                        "query {" +
                        "  viewer {" +
                        "   login" +
                        "  }" +
                        "}",
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + authToken)
                .post(requestBody)
                .url("https://api.github.com/graphql")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            String scopes = response.headers().get("X-OAuth-Scopes");
            response.close();
            return scopes;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "";
    }
}
