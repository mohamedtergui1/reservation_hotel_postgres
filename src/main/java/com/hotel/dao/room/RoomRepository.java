package com.hotel.dao.room;

import com.hotel.database.Orm;
import com.hotel.models.Reservation;
import com.hotel.models.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoomRepository extends Orm implements RoomRepositoryInterface  {
    public Class getEntityClass() {
        return Room.class;
    }
    @Override
    protected Set<Class<?>> manyToOneRelations() {
        return new HashSet<>(Arrays.asList(Reservation.class));
    }
    public boolean insert(Room room){return super.insert(room);}
    public boolean update(Room room){return super.update(room);}
    public boolean delete(Room room){return super.delete(room);}
    public ArrayList<Room> all(){return super.all();}
    public Room getById(Integer id){return((Room) super.getById(id));}
}
