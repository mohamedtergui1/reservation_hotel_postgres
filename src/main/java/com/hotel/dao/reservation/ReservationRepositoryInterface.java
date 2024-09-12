package com.hotel.dao.reservation;

import com.hotel.models.Reservation;

import java.util.ArrayList;

public interface ReservationRepositoryInterface {
    public boolean insert(Reservation reservation);
    public boolean update(Reservation reservation);
    public boolean delete(Reservation reservation);
    public ArrayList<Reservation> all();
    public Reservation getById(Integer id);
}
