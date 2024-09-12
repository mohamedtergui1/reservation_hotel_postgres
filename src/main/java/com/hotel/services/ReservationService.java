package com.hotel.services;

import com.hotel.interfaces.Count;
import com.hotel.models.Reservation;
import com.hotel.dao.reservation.ReservationRepositoryInterface;

import java.sql.SQLException;
import java.util.List;

public class ReservationService {
    private ReservationRepositoryInterface reservationRepository;
    private Count reservationCount;
    public ReservationService(ReservationRepositoryInterface reservationRepository, Count reservationCount) {
        this.reservationRepository = reservationRepository;
        this.reservationCount = reservationCount;
    }
    public boolean addReservation(Reservation reservation) {
        return reservationRepository.insert(reservation);
    }
    public boolean updateReservation(Reservation reservation) {
            return reservationRepository.update(reservation);
    }
    public boolean deleteReservation(Reservation reservation) {return reservationRepository.delete(reservation);}
    public List<Reservation> getAllReservations()  {
      System.out.println(reservationCount.count());
        return  reservationRepository.all(); }
    public Reservation getReservationById(Integer id) { return reservationRepository.getById(id); }
}