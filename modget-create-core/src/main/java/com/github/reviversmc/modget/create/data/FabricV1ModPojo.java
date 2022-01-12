package com.github.reviversmc.modget.create.data;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class FabricV1ModPojo {

    private String[] authors;
    private String name;
    private String description;
    private String id;
    private String version;

    private Contact contact = new Contact();
    private EntryPoint entryPoints = new EntryPoint();

    public FabricV1ModPojo() {

    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public EntryPoint getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(EntryPoint entryPoints) {
        this.entryPoints = entryPoints;
    }

    /*
        This is a huge dumping group for all values, as there can be so many dynamic values.
        Should never actually be used to get values!
        */
    @JsonAnySetter
    public void addJson(String key, Object value) {
        //Purposefully empty.
    }

    public static class Contact {

        private String email = "~";
        private String irc = "~";

        private String homepage = "~";
        private String sources = "~";
        private String issues = "~";

        private final Map<String, Object> others = new HashMap<>();

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIrc() {
            return irc;
        }

        public void setIrc(String irc) {
            this.irc = irc;
        }

        public String getHomepage() {
            return homepage;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }

        public String getSources() {
            return sources;
        }

        public void setSources(String sources) {
            this.sources = sources;
        }

        public String getIssues() {
            return issues;
        }

        public void setIssues(String issues) {
            this.issues = issues;
        }

        @JsonAnySetter
        public void addOthers(String key, Object value) {
            others.put(key, value);
        }

        public Map<String, Object> getOthers() {
            return others;
        }
    }

    /**
     * If the mod is in Java, all variables will be an array of {@link String}.
     * Else, it will be an array of {@link EntryPointObject}
     */
    public static class EntryPoint {
        private Object[] client;
        private Object[] main;
        private Object[] server;

        public Object[] getClient() {
            return client;
        }

        public void setClient(Object[] client) {
            this.client = client;
        }

        public Object[] getMain() {
            return main;
        }

        public void setMain(Object[] main) {
            this.main = main;
        }

        public Object[] getServer() {
            return server;
        }

        public void setServer(Object[] server) {
            this.server = server;
        }

        public static class EntryPointObject {
            private String adapter;
            private String value;

            public String getAdapter() {
                return adapter;
            }

            public void setAdapter(String adapter) {
                this.adapter = adapter;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @JsonAnySetter
            public void addJson(String key, Object value) {

            }
        }
    }
}
