package com.hotel.services;

import com.hotel.dao.room.RoomRepository;
import com.hotel.dao.room.RoomRepositoryInterface;
import com.hotel.models.Room;

public class RoomService {
    RoomRepositoryInterface roomRepository;
    public RoomService(RoomRepositoryInterface roomRepository) {
        this.roomRepository = roomRepository;
    }
    public Room getById(int id){
        return roomRepository.getById(id);
    }
}
