package com.hotel.controllers;

import com.hotel.models.Reservation;
import com.hotel.repositoryInterfaces.ReservationRepositoryInterface;
import com.hotel.services.reservation.ReservationService;

import java.util.List;

public class ReservationController {
     ReservationService reservationService;
     public ReservationController(ReservationService reservationService) {
          this.reservationService = reservationService;
     }

     public void index()
     {
          List < Reservation> reservations = this.reservationService.getAllReservations();
               for (Reservation reservation : reservations) {
                    System.out.println(reservation.toString());
               }
     }
}
