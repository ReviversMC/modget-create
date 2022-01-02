package com.github.reviversmc.modget.create.apicalls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.create.data.ModrinthV1ModPojo;
import com.github.reviversmc.modget.create.data.ModrinthV1TeamMemberPojo;
import com.github.reviversmc.modget.create.data.ModrinthV1UserPojo;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.inject.Named;
import java.io.IOException;

public class ModrinthV1Query implements ModrinthQuery {

    private final ObjectMapper jsonMapper;
    private final OkHttpClient okHttpClient;
    private final String projectId;

    @AssistedInject
    public ModrinthV1Query(
            @Named("json") ObjectMapper jsonMapper,
            OkHttpClient okHttpClient,
            @Assisted String projectId
    ) {
        this.jsonMapper = jsonMapper;
        this.okHttpClient = okHttpClient;
        this.projectId = projectId;
    }

    @Override
    public ModrinthV1ModPojo getMod() {
        if (modExists()) {
            Request request = new Request.Builder()
                    .url("https://api.modrinth.com/api/v1/mod/" + projectId)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    String responseBodyString = responseBody.string();
                    response.close();
                    return jsonMapper.readValue(responseBodyString, ModrinthV1ModPojo.class);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ModrinthV1ModPojo();
    }

    @Override
    public ModrinthV1TeamMemberPojo[] getTeamMembers() {
        if (modExists()) {
            ModrinthV1ModPojo modPojo = getMod();
            Request request = new Request.Builder()
                    .url("https://api.modrinth.com/api/v1/team/" + modPojo.getTeam() + "/members")
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    String responseBodyString = responseBody.string();
                    response.close();
                    return jsonMapper.readValue(responseBodyString, ModrinthV1TeamMemberPojo[].class);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ModrinthV1TeamMemberPojo[0];
    }

    @Override
    public ModrinthV1UserPojo getOwner() {
        if (modExists()) {
            ModrinthV1TeamMemberPojo[] teamMemberPojos = getTeamMembers();
            for (ModrinthV1TeamMemberPojo teamMemberPojo : teamMemberPojos) {
                if (!teamMemberPojo.getRole().equalsIgnoreCase("Owner")) continue;

                Request request = new Request.Builder()
                        .url("https://api.modrinth.com/api/v1/user/" + teamMemberPojo.getUserId())
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    ResponseBody responseBody = response.body();

                    if (responseBody != null) {
                        String responseBodyString = responseBody.string();
                        response.close();
                        return jsonMapper.readValue(responseBodyString, ModrinthV1UserPojo.class);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return new ModrinthV1UserPojo();
    }

    @Override
    public boolean modExists() {
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
