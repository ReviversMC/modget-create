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
import java.util.Optional;

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
    public Optional<ModrinthV1ModPojo> getMod() {
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
                    return Optional.of(
                            jsonMapper.readValue(responseBodyString, ModrinthV1ModPojo.class)
                    );
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public ModrinthV1TeamMemberPojo[] getTeamMembers() {
        if (modExists()) {

            Optional<ModrinthV1ModPojo> optionalModPojo = getMod();
            if (optionalModPojo.isEmpty()) return new ModrinthV1TeamMemberPojo[0];

            Request request = new Request.Builder()
                    .url("https://api.modrinth.com/api/v1/team/" +
                            optionalModPojo.get().getTeam() + "/members")
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
    public Optional<ModrinthV1UserPojo> getOwner() {
        if (modExists()) {
            ModrinthV1TeamMemberPojo[] teamMemberPojos = getTeamMembers();
            if (teamMemberPojos.length == 0) return Optional.empty();

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
                        return Optional.of(
                                jsonMapper.readValue(responseBodyString, ModrinthV1UserPojo.class)
                        );
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return Optional.empty();
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
                if (!responseBody.string().equals("")) {
                    response.close();
                    return true;
                }
            }

            response.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
