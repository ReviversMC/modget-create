package com.github.reviversmc.modget.create.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@SuppressWarnings("unused")
public class ModrinthV1ModPojo {

    private String id;
    private String slug;
    private String team;

    private String title;
    private String description;
    private String body;

    private String published;
    private String updated;
    private String status;
    private License license;

    //Support is one of "required", "optional", "unsupported", or "unknown".
    private String clientSide;
    private String serverSide;

    private int downloads;
    private int followers;

    private String[] categories;
    private String[] versions;

    private String iconUrl;
    private String issuesUrl;
    private String sourceUrl;
    private String wikiUrl;
    private String discordUrl;
    private DonationLink[] donationUrls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Deprecated //As stated by Modrinth
    @JsonProperty("body_url")
    public void setBodyUrl(String bodyUrl) {
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    @JsonProperty("client_side")
    public String getClientSide() {
        return clientSide;
    }

    @JsonProperty("client_side")
    public void setClientSide(String clientSide) {
        this.clientSide = clientSide;
    }

    @JsonProperty("server_side")
    public String getServerSide() {
        return serverSide;
    }

    @JsonProperty("server_side")
    public void setServerSide(String serverSide) {
        this.serverSide = serverSide;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String[] getVersions() {
        return versions;
    }

    public void setVersions(String[] versions) {
        this.versions = versions;
    }

    @JsonProperty("icon_url")
    public Optional<String> getIconUrl() {
        return iconUrl == null ? Optional.empty() : Optional.of(iconUrl);
    }

    @JsonProperty("icon_url")
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @JsonProperty("issues_url")
    public Optional<String> getIssuesUrl() {
        return issuesUrl == null ? Optional.empty() : Optional.of(issuesUrl);
    }

    @JsonProperty("issues_url")
    public void setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
    }

    @JsonProperty("source_url")
    public Optional<String> getSourceUrl() {
        return sourceUrl == null ? Optional.empty() : Optional.of(sourceUrl);
    }

    @JsonProperty("source_url")
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @JsonProperty("wiki_url")
    public Optional<String> getWikiUrl() {
        return wikiUrl == null ? Optional.empty() : Optional.of(wikiUrl);
    }

    @JsonProperty("wiki_url")
    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    @JsonProperty("discord_url")
    public Optional<String> getDiscordUrl() {
        return discordUrl == null ? Optional.empty() : Optional.of(discordUrl);
    }

    @JsonProperty("discord_url")
    public void setDiscordUrl(String discordUrl) {
        this.discordUrl = discordUrl;
    }

    @JsonProperty("donation_urls")
    public DonationLink[] getDonationUrls() {
        return donationUrls;
    }

    @JsonProperty("donation_urls")
    public void setDonationUrls(DonationLink[] donationUrls) {
        this.donationUrls = donationUrls;
    }

    public static class License {
        private String id;
        private String name;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class DonationLink {
        private String id;
        private String platform;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
