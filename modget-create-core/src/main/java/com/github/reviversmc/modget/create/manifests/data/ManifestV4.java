package com.github.reviversmc.modget.create.manifests.data;

@SuppressWarnings("unused")
public class ManifestV4 {

    private String publisher;
    private String[] iconUrls;
    private String status;
    private UpdateAlternative[] updateAlternatives;

    private String name;
    private String description;
    private Author[] authors;

    private String home;
    private String source;
    private String issues;
    private String support;
    private String wiki;

    private Version[] versions;

    public int getManifestSpecVersion() {
        return 4;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String[] getIconUrls() {
        return iconUrls;
    }

    public void setIconUrls(String[] iconUrls) {
        this.iconUrls = iconUrls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpdateAlternative[] getUpdateAlternatives() {
        return updateAlternatives;
    }

    public void setUpdateAlternatives(UpdateAlternative[] updateAlternatives) {
        this.updateAlternatives = updateAlternatives;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public Version[] getVersions() {
        return versions;
    }

    public void setVersions(Version[] versions) {
        this.versions = versions;
    }

    private static class Author {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class Chats {
        private String discord;
        private String irc;
        private MiscChats[] others;

        public String getDiscord() {
            return discord;
        }

        public void setDiscord(String discord) {
            this.discord = discord;
        }

        public String getIrc() {
            return irc;
        }

        public void setIrc(String irc) {
            this.irc = irc;
        }

        public MiscChats[] getOthers() {
            return others;
        }

        public void setOthers(MiscChats[] others) {
            this.others = others;
        }

        private static class MiscChats {
            private String name;
            private String url;

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
    }

    private static class UpdateAlternative {
        private String packageId;

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }
    }

    private static class Version {
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }


}
