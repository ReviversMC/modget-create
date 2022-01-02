package com.github.reviversmc.modget.create.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@SuppressWarnings("unused")
public class ModrinthV1UserPojo {
    private String id;
    private String username;
    private String name;
    private String email;
    private String avatarUrl;
    private String bio;
    private String created;
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //We should never be able to get this property, nor need to do anything with it.
    @JsonProperty("github_id")
    public void setGithubId(String githubId) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getEmail() {
        return email == null ? Optional.empty() : Optional.of(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("avatar_url")
    public Optional<String> getAvatarUrl() {
        return avatarUrl == null ? Optional.empty() : Optional.of(avatarUrl);
    }

    @JsonProperty("avatar_url")
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
