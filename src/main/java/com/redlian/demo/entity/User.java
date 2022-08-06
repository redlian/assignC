package com.redlian.demo.entity;

import lombok.Getter;

public class User {
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getWks() {
        return this.wks;
    }

    public void setWks(final String wks) {
        this.wks = wks;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    private String id;
    private String name;
    private String wks;
    private String time;
}
