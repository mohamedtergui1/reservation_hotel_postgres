package com.hotel.controllers;

import com.hotel.models.Reservation;
import com.hotel.services.ReservationService;

public class ReservationController {
     ReservationService reservationService;
     public ReservationController(ReservationService reservationService) {
          this.reservationService = reservationService;
     }

     public void index()
     {
          System.out.println("reservations list");
          boolean a = false;
          for(Reservation reservation : reservationService.getAllReservations()) {
               if(a)
               {
                    System.out.println(",");
               }
               a = true;
                    System.out.println(reservation);
          }
     }

}
