package com.hotel.models;

import com.hotel.interfaces.GetId;

import java.sql.Date; // Import java.sql.Date

public class Reservation implements GetId {
    private int id;
    private Room room;
    private User user;
    private Date check_in_date; // Change to java.sql.Date
    private Date check_out_date; // Change to java.sql.Date

    // Getters and setters for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getters and setters for room
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    // Getters and setters for check_in_date
    public Date getCheckInDate() {
        return check_in_date;
    }

    public void setCheckInDate(Date check_in_date) {
        this.check_in_date = check_in_date;
    }

    // Getters and setters for check_out_date
    public Date getCheckOutDate() {
        return check_out_date;
    }

    public void setCheckOutDate(Date check_out_date) {
        this.check_out_date = check_out_date;
    }
}
