package com.hotel;

import com.hotel.dao.ReservationRepository;
import com.hotel.models.Reservation;
import com.hotel.services.reservation.ReservationService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            System.out.println("HOTEL tergui");
        ReservationService reservationService = new ReservationService(new ReservationRepository());
        List<Reservation> reservations = new ArrayList<>();
        for(Reservation reservation : reservationService.getAllReservations()) {
            System.out.println(reservation.toString());
        }

    }
}