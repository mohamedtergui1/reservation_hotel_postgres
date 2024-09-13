package com.hotel.services;

import com.hotel.dao.room.RoomRepository;
import com.hotel.dao.room.RoomRepositoryInterface;
import com.hotel.models.Room;

import java.util.List;

public class RoomService {
    RoomRepositoryInterface roomRepository;
    public RoomService(RoomRepositoryInterface roomRepository) {
        this.roomRepository = roomRepository;
    }
    public Room getById(int id){
        return roomRepository.getById(id);
    }
    public List<Room> getAll(){
        return roomRepository.all();
    }
    public boolean updateRoom(Room room){
        return roomRepository.update(room);
    }
    public boolean deleteRoom(Room room){
        return roomRepository.delete(room);
    }
    public boolean addRoom(Room room){
        return roomRepository.insert(room);
    }
}
