package com.example.yunyi.projecthydra;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1000593 on 12/12/16.
 */

public class Plant {

    private String type;
    private String name;
    private String id;
    private String waterNeeds;
    private String sunNeeds;

    public Plant(String type, String name, String id) {
        this.type = type;
        this.id = id;
        this.name = null;
        this.waterNeeds = "40";
        this.sunNeeds = "50";
        this.name = name;
    }
    public Plant() {

    }
    public Plant(String type, String id) {
        this.type = type;
        this.id = id;
        this.name = null;
        this.waterNeeds = "40";
        this.sunNeeds = "50";
    }

    public String getWaterNeeds() {
        return waterNeeds;
    }

    public void setWaterNeeds(String waterNeeds) {
        this.waterNeeds = waterNeeds;
    }

    public String getSunNeeds() {
        return sunNeeds;
    }

    public void setSunNeeds(String sunNeeds) {
        this.sunNeeds = sunNeeds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("waterNeeds", waterNeeds);
        result.put("sunNeeds", sunNeeds);
        result.put("type", type);


        return result;
    }
}
