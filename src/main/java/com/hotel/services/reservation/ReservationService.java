package com.hotel.services.reservation;

import com.hotel.repositoryInterfaces.ReservationRepositoryInterface;
import com.hotel.models.Reservation;

import java.util.List;

public class ReservationService {
    ReservationRepositoryInterface reservationRepository;
    public ReservationService(ReservationRepositoryInterface reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    public boolean addReservation(Reservation reservation) {
        return reservationRepository.insert(reservation);
    }
    public  boolean updateReservation(Reservation reservation) {
            return reservationRepository.update(reservation);
    }
    public boolean deleteReservation(Reservation reservation) {
        return reservationRepository.delete(reservation);
    }
    public List<Reservation> getAllReservations() {
        return  reservationRepository.all();
    }
    public Reservation findReservationById(Integer id) {
        return reservationRepository.getById(id);
    }
}