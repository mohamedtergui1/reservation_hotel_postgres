package com.hotel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLDatabase {
    private static PostgreSQLDatabase instance;
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;


    private PostgreSQLDatabase(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connect();
    }


    public static synchronized PostgreSQLDatabase getInstance(String url, String user, String password) {
        if (instance == null) {
            instance = new PostgreSQLDatabase(url, user, password);
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }


    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }



}
