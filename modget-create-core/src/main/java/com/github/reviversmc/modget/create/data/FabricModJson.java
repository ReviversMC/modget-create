package com.github.reviversmc.modget.create.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class FabricModJson {

    private String name;
    private String description;
    private String id;
    private String version;

    //this.json is a huge dumping group for all values, as there can be so many dynamic values.
    private final Map<String, Object> json = new HashMap<>();

    public FabricModJson() {

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
