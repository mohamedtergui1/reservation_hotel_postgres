package com.hotel.models;

import com.hotel.interfaces.GetId;

public class Hotel implements GetId {
    private int id;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{"
                + "\"id\": " + id + ", "
                + "\"name\": \"" + name + "\""
                + "}";
    }
}
