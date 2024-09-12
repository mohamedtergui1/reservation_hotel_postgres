package com.hotel.services;

import com.hotel.models.Reservation;
import com.hotel.dao.reservation.ReservationRepositoryInterface;

import java.util.List;

public class ReservationService {
    ReservationRepositoryInterface reservationRepository;
    public ReservationService(ReservationRepositoryInterface reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    public boolean addReservation(Reservation reservation) {
        return reservationRepository.insert(reservation);
    }
    public boolean updateReservation(Reservation reservation) {
            return reservationRepository.update(reservation);
    }
    public boolean deleteReservation(Reservation reservation) {
        return reservationRepository.delete(reservation);
    }
    public List<Reservation> getAllReservations() { return  reservationRepository.all(); }
    public Reservation getReservationById(Integer id) { return reservationRepository.getById(id); }
}