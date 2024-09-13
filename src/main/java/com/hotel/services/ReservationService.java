package com.hotel.services;

import com.hotel.interfaces.Count;
import com.hotel.interfaces.CountAvecConditions;
import com.hotel.models.Reservation;
import com.hotel.dao.reservation.ReservationRepositoryInterface;

import java.util.List;

public class ReservationService {
    private ReservationRepositoryInterface reservationRepository;
    private Count reservationCount;
    private CountAvecConditions reservationCountAvecConditions;
    public ReservationService(ReservationRepositoryInterface reservationRepository, Count reservationCount, CountAvecConditions countAvecConditions) {
        this.reservationRepository = reservationRepository;
        this.reservationCount = reservationCount;
        this.reservationCountAvecConditions = countAvecConditions;
    }
    public boolean addReservation(Reservation reservation) {
        return reservationRepository.insert(reservation);
    }
    public boolean updateReservation(Reservation reservation) {
            return reservationRepository.update(reservation);
    }
    public boolean deleteReservation(Reservation reservation) { return reservationRepository.delete(reservation); }
    public List<Reservation> getAllReservations()  { return  reservationRepository.all(); }
    public Reservation getReservationById(Integer id) { return reservationRepository.getById(id); }
    public int count(){ return reservationCount.count(); }
}