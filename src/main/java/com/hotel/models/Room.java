package com.hotel.models;

import com.hotel.interfaces.GetId;

public class Room implements GetId {
    private int id;              // Corresponds to the "id" column in SQL table
    private String room_number; // Changed to match the "room_number" column in SQL table
    private String room_type;   // Changed to match the "room_type" column in SQL table
    private float price;       // Corresponds to the "price" column in SQL table

    // Getters and setters for id
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for room_number
    public String getRoomNumber() {
        return room_number;
    }

    public void setRoomNumber(String room_number) {
        this.room_number = room_number;
    }

    // Getters and setters for room_type
    public String getRoomType() {
        return room_type;
    }

    public void setRoomType(String room_type) {
        this.room_type = room_type;
    }

    // Getters and setters for price
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
}
