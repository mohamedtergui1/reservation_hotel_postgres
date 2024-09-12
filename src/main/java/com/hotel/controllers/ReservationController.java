     package com.hotel.controllers;

     import com.hotel.dao.room.RoomRepository;
     import com.hotel.dao.user.CustomerRepository;
     import com.hotel.models.Customer;
     import com.hotel.models.Reservation;
     import com.hotel.models.Room;
     import com.hotel.services.CustomerService;
     import com.hotel.services.ReservationService;
     import com.hotel.services.RoomService;

     import java.sql.Date;
     import java.time.LocalDate;
     import java.time.temporal.ChronoUnit;
     import java.time.temporal.Temporal;
     import java.util.InputMismatchException;
     import java.util.List;
     import java.util.Scanner;

     public class ReservationController {
          private final ReservationService reservationService;
          private final Scanner scanner;

          public ReservationController(ReservationService reservationService) {
               this.reservationService = reservationService;
               this.scanner = new Scanner(System.in);
          }

          public void index()   {
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
               Reservation reservation = reservationService.getReservationById(id);
               boolean success = reservationService.deleteReservation(reservation);
               if (success) {
                    long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), (Temporal) reservation.getCheckInDate());
                    Customer customer = reservation.getCustomer();
                    if (daysBetween < 4)
                    {
                         customer.setWallet(customer.getWallet() + reservation.getRoom().getPrice());
                         new CustomerService(new CustomerRepository()).updateCustomer(customer);
                    }
                    else{
                         customer.setWallet(customer.getWallet() + (reservation.getRoom().getPrice() * ((float) 8 /10)));
                         new CustomerService(new CustomerRepository()).updateCustomer(customer);
                    }
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
               Room room = null;
               Customer customer = null;

               // Validate Room ID
               while (room == null) {
                    try {
                         System.out.print("Enter room ID: ");
                         int roomId = scanner.nextInt();
                         scanner.nextLine(); // Consume newline

                         room = new RoomService(new RoomRepository()).getById(roomId);
                         if (room == null) {
                              System.out.println("Room with ID " + roomId + " not found. Please enter a valid room ID.");
                         } else {
                              reservation.setRoom(room);
                         }
                    } catch (InputMismatchException e) {
                         System.out.println("Invalid input. Please enter a numeric value for room ID.");
                         scanner.nextLine(); // Clear the invalid input
                    }
               }

               // Validate Customer ID
               while (customer == null) {
                    try {
                         System.out.print("Enter customer ID: ");
                         int customerId = scanner.nextInt();
                         scanner.nextLine(); // Consume newline

                         customer = new CustomerService(new CustomerRepository()).getCustomer(customerId);
                         if (customer == null) {
                              System.out.println("Customer with ID " + customerId + " not found. Please enter a valid customer ID.");
                         } else {
                              reservation.setCustomer(customer);
                         }
                    } catch (InputMismatchException e) {
                         System.out.println("Invalid input. Please enter a numeric value for customer ID.");
                         scanner.nextLine(); // Clear the invalid input
                    }
               }

               // Validate Check-In Date
               Date checkInDate = null;
               while (checkInDate == null) {
                    try {
                         System.out.print("Enter check-in date (yyyy-MM-dd): ");
                         String checkInDateInput = scanner.nextLine();
                         checkInDate = Date.valueOf(checkInDateInput);

                         // Check if the date is valid
                         if (checkInDate.before(new Date(System.currentTimeMillis()))) {
                              System.out.println("Check-in date cannot be in the past. Please enter a valid date.");
                              checkInDate = null;
                         } else {
                              reservation.setCheckInDate(checkInDate);
                         }
                    } catch (IllegalArgumentException e) {
                         System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                    }
               }

               // Validate Check-Out Date
               Date checkOutDate = null;
               while (checkOutDate == null) {
                    try {
                         System.out.print("Enter check-out date (yyyy-MM-dd): ");
                         String checkOutDateInput = scanner.nextLine();
                         checkOutDate = Date.valueOf(checkOutDateInput);

                         // Check if the date is valid
                         if (checkOutDate.before(checkInDate)) {
                              System.out.println("Check-out date must be after check-in date. Please enter a valid date.");
                              checkOutDate = null;
                         } else {
                              reservation.setCheckOutDate(checkOutDate);
                         }
                    } catch (IllegalArgumentException e) {
                         System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                    }
               }
          }
     }