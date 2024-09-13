package com.hotel.models;

import com.hotel.interfaces.GetId;
import java.sql.Date; // Import java.sql.Date


public class Reservation implements GetId {
    private int id;
    private Room room;
    private Customer customer;
    private Date check_in_date; // Change to java.sql.Date
    private Date check_out_date; // Change to java.sql.Date
    private boolean is_cancelled;

    public boolean getIs_cancelled() {
        return is_cancelled;
    }

    public void setIs_cancelled(boolean is_cancelled) {
        this.is_cancelled = is_cancelled;
    }


    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"room\": " + (room != null ? "\"" + room + "\"" : "null") + ",\n" +
                "  \"customer\": " + (customer != null ? "\"" + customer + "\"" : "null") + ",\n" +
                "  \"check_in_date\": " + (check_in_date != null ? "\"" + check_in_date + "\"" : "null") + ",\n" +
                "  \"check_out_date\": " + (check_out_date != null ? "\"" + check_out_date + "\"" : "null") + "\n" +
                "}";
    }


    // Getters and setters for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for customer
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
