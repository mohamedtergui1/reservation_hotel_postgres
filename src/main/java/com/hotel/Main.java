package com.hotel;

import com.hotel.controllers.ReservationController;
import com.hotel.dao.reservation.ReservationRepository;
import com.hotel.dao.room.RoomRepository;
import com.hotel.models.Reservation;
import com.hotel.services.ReservationService;

import java.util.*;


public class Main {
    public static void main(String[] args) {


        RoomRepository roomRepository = new RoomRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
         List<Reservation> reservations = (List<Reservation>) roomRepository.loadRelations(reservationRepository.getById(1)).get("reservations");
         if(reservations != null && !reservations.isEmpty())
         {
             for (Reservation reservation : reservations) {
                 System.out.println(reservation);
             }
         }


        System.out.println("HOTEL Reservation Management");
        Scanner scanner = new Scanner(System.in);
        int i;
           System.out.println("menu");

           do {
               System.out.println("1. mange Reservations");
               System.out.println("2. mange Rooms");
               System.out.println("3. mange Hotels");
               System.out.println("4. mange users");
               System.out.println("5. exit");
               System.out.print("Enter your choice: ");
               i=scanner.nextInt();
               scanner.nextLine();
               switch (i)
               {
                   case 1:
                       new ReservationController(new ReservationService(new ReservationRepository())).index();
                       break;
               }
           } while (5 != i );
    }
}