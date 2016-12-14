package com.example.yunyi.projecthydra;

/**
 * Created by 1000593 on 13/12/16.
 */

public class Logs {

    public String moisture;
    public String time;
    public String wHeight;
    public int moistureI;

    public Logs() {}

    public Logs(String moisture, String time, String wHeight) {
        this.moisture = moisture;
        this.time = time;
        this.wHeight = wHeight;
        this.moistureI = Integer.parseInt(this.moisture);
    }
}

