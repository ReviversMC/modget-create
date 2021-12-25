package com.github.reviversmc.modget.create.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class FabricModPojo {

    private String[] authors;
    private String name;
    private String description;
    private String id;
    private String version;

    //this.json is a huge dumping group for all values, as there can be so many dynamic values.
    private final Map<String, Object> json = new HashMap<>();

    public FabricModPojo() {

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

    @JsonAnySetter
    public void addJson(String key, Object value) {
        json.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getJson() {
        return json;
    }
}
