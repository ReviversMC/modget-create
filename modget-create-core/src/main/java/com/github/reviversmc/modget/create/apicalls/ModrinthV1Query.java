package com.github.reviversmc.modget.create.apicalls;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class ModrinthV1Query implements ModrinthQuery {

    private final OkHttpClient okHttpClient;
    private final String projectId;

    @AssistedInject
    public ModrinthV1Query(OkHttpClient okHttpClient, @Assisted String projectId) {
        this.okHttpClient = okHttpClient;
        this.projectId = projectId;
    }


    @Override
    public boolean projectExists() {
        Request request = new Request.Builder()
                .url("https://api.modrinth.com/api/v1/mod/" + projectId)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                response.close();
                return true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
