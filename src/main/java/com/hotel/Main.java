package com.hotel;

import com.hotel.controllers.ReservationController;
import com.hotel.dao.reservation.ReservationRepository;
import com.hotel.dao.room.RoomRepository;
import com.hotel.models.Reservation;
import com.hotel.services.ReservationService;

import java.sql.SQLException;
import java.util.*;


public class Main {
    public static void main(String[] args)  {

        System.out.println("HOTELS Reservation Management");
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
                       new ReservationController(new ReservationService(new ReservationRepository(), new ReservationRepository(),new ReservationRepository())).index();
                       break;
               }
           } while (5 != i);
    }
}