package com.hotel.dao.reservation;

import com.hotel.database.Orm;
import com.hotel.interfaces.Count;
import com.hotel.interfaces.CountAvecConditions;
import com.hotel.models.Reservation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ReservationRepository extends Orm implements ReservationRepositoryInterface, Count, CountAvecConditions {
    @Override
    protected  Class getEntityClass()
    {
        return Reservation.class;
    }
    public boolean insert(Reservation reservation) {
        return super.insert(reservation);
    }
    public boolean update(Reservation reservation) {
            return  super.update(reservation);
    }
    public boolean delete(Reservation reservation) { return super.delete(reservation); }
    public ArrayList<Reservation> all() {
        return super.all();
    }
    public Reservation getById(Integer id) {
        return ((Reservation) super.getById(id));
    }
    @Override
    public int count()  {return super.count();}
    @Override
    public  int countAvecConditions( HashMap<String, HashMap<String, ArrayList<String>>> conditions) {return super.count(getEntityClass(), conditions);}
}