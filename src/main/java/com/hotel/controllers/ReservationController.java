     package com.hotel.controllers;

     import com.hotel.models.Customer;
     import com.hotel.models.Reservation;
     import com.hotel.models.Room;
     import com.hotel.services.ReservationService;
     import java.sql.Date;
     import java.util.List;
     import java.util.Scanner;

     public class ReservationController {
          private final ReservationService reservationService;
          private final Scanner scanner;

          public ReservationController(ReservationService reservationService) {
               this.reservationService = reservationService;
               this.scanner = new Scanner(System.in);
          }

          public void index() {
               System.out.println("Reservations List:");
               List<Reservation> reservations = reservationService.getAllReservations();

               if (reservations.isEmpty()) {
                    System.out.println("No reservations found.");
               } else {
                    for (int i = 0; i < reservations.size(); i++) {
                         System.out.println((i + 1) + ". " + reservations.get(i));
                    }
               }
               printMenu();
          }

          private void printMenu() {
               System.out.println("\nMenu:");
               System.out.println("1. Add Reservation");
               System.out.println("2. Cancel Reservation");
               System.out.println("3. Edit Reservation");
               System.out.println("4. Exit");
               System.out.print("Please select an option (1-4): ");

               int choice = scanner.nextInt();
               scanner.nextLine(); // Consume newline

               switch (choice) {
                    case 1:
                         addReservation();
                         break;
                    case 2:
                         cancelReservation();
                         break;
                    case 3:
                         editReservation();
                         break;
                    case 4:
                         System.out.println("Exiting...");
                         break;
                    default:
                         System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                         printMenu();
                         break;
               }
          }

          private void addReservation() {
               System.out.println("Please provide the following details to add a new reservation:");

               Reservation reservation = new Reservation();
               populateReservationDetails(reservation);

               reservationService.addReservation(reservation);
               System.out.println("Reservation has been added successfully.");
          }

          private void cancelReservation() {
               System.out.print("Enter the reservation ID you want to cancel: ");
               int id = scanner.nextInt();
               scanner.nextLine();
               boolean success = reservationService.deleteReservation(reservationService.getReservationById(id));
               if (success) {
                    System.out.println("Reservation with ID " + id + " has been canceled successfully.");
               } else {
                    System.out.println("Reservation with ID " + id + " not found.");
               }
          }

          private void editReservation() {
               System.out.print("Enter the reservation ID you want to edit: ");
               int id = scanner.nextInt();
               scanner.nextLine(); // Consume newline

               Reservation reservation = reservationService.getReservationById(id);
               if (reservation != null) {
                    System.out.println("Current details of reservation ID " + id + ":");
                    System.out.println(reservation);
                    System.out.println("Please provide new details:");

                    populateReservationDetails(reservation);

                    reservationService.updateReservation(reservation);
                    System.out.println("Reservation has been updated successfully.");
               } else {
                    System.out.println("Reservation with ID " + id + " not found.");
               }
          }

          private void populateReservationDetails(Reservation reservation) {
               System.out.print("Enter room details (e.g., Room number): ");
               Room room = new Room();
               room.setId(scanner.nextInt());
               scanner.nextLine();
               reservation.setRoom(room);

               System.out.print("Enter customer details (e.g., Customer name): ");

               Customer customer = reservation.getCustomer();
               customer.setId(scanner.nextInt());
               scanner.nextLine();
               reservation.setCustomer(customer);

               System.out.print("Enter check-in date (yyyy-mm-dd): ");
               String checkInDateInput = scanner.nextLine();
               reservation.setCheckInDate(Date.valueOf(checkInDateInput));

               System.out.print("Enter check-out date (yyyy-mm-dd): ");
               String checkOutDateInput = scanner.nextLine();
               reservation.setCheckOutDate(Date.valueOf(checkOutDateInput));
          }
     }