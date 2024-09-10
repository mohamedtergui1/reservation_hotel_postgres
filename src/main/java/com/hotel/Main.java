package com.hotel;

import com.hotel.dao.ReservationRepository;
import com.hotel.models.Reservation;
import com.hotel.models.Room;
import com.hotel.models.Customer;
import com.hotel.services.ReservationService;
import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        // Create a User object
        Customer user = new Customer();
        user.setId(1);
        user.setEmail("csa@example.com");
        user.setName("John Doe");
        user.setPassword("securepassword");

        // Create a Room object
        Room room = new Room();
        room.setId(2);
        room.setRoomNumber("101");
        room.setRoomType("Single");
        room.setPrice(99);

        // Create a Reservation object
        Reservation reservation = new Reservation();
        reservation.setUser(user); // Set the User object
        reservation.setRoom(room); // Set the Room object
        reservation.setCheckInDate(Date.valueOf("2024-10-01")); // Check-in date as java.sql.Date
        reservation.setCheckOutDate(Date.valueOf("2024-10-05")); // Chec

        // Initialize ReservationService with a ReservationRepository
        ReservationService reservationService = new ReservationService(new ReservationRepository());
        //reservation.setId(12);
            Reservation reservation1  = reservationService.findReservationById(8);
            System.out.println(reservation1.getId());
        System.out.println(reservation1.getCheckInDate());
        System.out.println(reservation1.getCheckOutDate());
        System.out.println(reservation1.getRoom().getRoomType());
        System.out.println(reservation1.getUser().getEmail());


            //        List<Reservation> reservations = reservationService.getAllReservations();
//        for (Reservation reservation1 : reservations) {
//            System.out.println(reservation1.toString());
//        }

    }
}