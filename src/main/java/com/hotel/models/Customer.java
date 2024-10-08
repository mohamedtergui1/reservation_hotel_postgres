package com.hotel.models;

import com.hotel.interfaces.GetId;

public class Customer implements GetId {
    private String email;
    private String name;
    private String password;
    private int id;
    private float wallet;


    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }



    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters and setters for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and setters for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"name\": \"" + (name != null ? name.replace("\"", "\\\"") : "null") + "\",\n" +
                "  \"email\": \"" + (email != null ? email.replace("\"", "\\\"") : "null") + "\"\n" +
                "  \"wallet\": \"" +  wallet + "\"\n" +
                "}";
    }

}