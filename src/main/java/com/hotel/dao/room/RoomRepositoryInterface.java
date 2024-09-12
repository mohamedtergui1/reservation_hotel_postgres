package com.hotel.dao.room;

import com.hotel.models.Room;

import java.util.ArrayList;

public interface RoomRepositoryInterface {
    public boolean insert(Room room);
    public boolean update(Room room);
    public boolean delete(Room room);
    public ArrayList<Room> all();
    public Room getById(Integer id);
}
