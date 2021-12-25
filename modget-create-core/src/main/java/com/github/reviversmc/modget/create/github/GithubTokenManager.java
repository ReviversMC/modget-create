package com.github.reviversmc.modget.create.github;

import static com.diogonunes.jcolor.Ansi.colorize;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.diogonunes.jcolor.Attribute;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Singleton
public class GithubTokenManager implements TokenManager {
    private final OkHttpClient okHttpClient;
    private String githubToken;

    @Inject
    public GithubTokenManager(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public Optional<String> getToken() {
        if (githubToken != null) return Optional.of(githubToken);
        return Optional.empty();
    }

    @Override
    public boolean setToken(String token) {
        if (validateToken(token)) {
            this.githubToken = token;
            return true;
        }
        return false;
    }

    @Override
    public boolean validateToken(String token) {
        //Start by checking the scope.
        RequestBody requestBody = RequestBody.create(
                GithubQuery.CONFIRM_LOGIN.getQuery(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .post(requestBody)
                .url("https://api.github.com/graphql")
                .build();

        try {

            Response response = okHttpClient.newCall(request).execute();

            String scopes = response.headers().get("X-OAuth-Scopes");
            if (scopes == null) return false;

            boolean publicRepo = false;
            boolean readUser = false;

            for (String scope : scopes.split(", ")) {
                if (scope.equals("public_repo")) publicRepo = true;
                if (scope.equals("read:user")) readUser = true;
            }

            return publicRepo && readUser;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
