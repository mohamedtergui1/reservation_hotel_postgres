package com.hotel.controllers;

import com.hotel.models.Room;
import com.hotel.services.RoomService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RoomController {

    private RoomService roomService;
    private Scanner scanner = new Scanner(System.in);

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Menu method
    public void showMenu() {
        while (true) {
            System.out.println("Room Management Menu:");
            System.out.println("1. Create Room");
            System.out.println("2. Edit Room");
            System.out.println("3. Delete Room");
            System.out.println("4. View All Rooms");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    createRoom();
                    break;
                case 2:
                    editRoom();
                    break;
                case 3:
                    deleteRoom();
                    break;
                case 4:
                    viewAllRooms();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createRoom() {
        System.out.println("Creating a new room.");
        // Collect room details from user
        Room room = new Room();
        System.out.print("Enter room number: ");
        room.setRoomNumber(scanner.nextLine());
        System.out.print("Enter room type: ");
        room.setRoomType(scanner.nextLine());
        System.out.print("Enter price: ");
        try {
            room.setPrice(scanner.nextFloat());
        } catch (InputMismatchException e) {
            System.out.println("Invalid price input. Please enter a valid number.");
            scanner.nextLine(); // Consume the invalid input
            return;
        }
        scanner.nextLine(); // Consume newline

        // Optionally, set the hotel if needed
        // System.out.print("Enter hotel ID: ");
        // int hotelId = scanner.nextInt();
        // scanner.nextLine();
        // Hotel hotel = hotelRepository.getHotelById(hotelId);
        // room.setHotel(hotel);

        // Add room using RoomService
        roomService.addRoom(room);
        System.out.println("Room created successfully.");
    }

    private void editRoom() {
        System.out.print("Enter the ID of the room you want to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Retrieve existing room
        Room room = roomService.getById(id);
        if (room != null) {
            System.out.println("Editing room with ID " + id);
            System.out.print("Enter new room number (leave blank to keep current): ");
            String roomNumber = scanner.nextLine();
            if (!roomNumber.isEmpty()) room.setRoomNumber(roomNumber);

            System.out.print("Enter new room type (leave blank to keep current): ");
            String roomType = scanner.nextLine();
            if (!roomType.isEmpty()) room.setRoomType(roomType);

            System.out.print("Enter new price (leave blank to keep current): ");
            String priceInput = scanner.nextLine();
            if (!priceInput.isEmpty()) {
                try {
                    room.setPrice(Float.parseFloat(priceInput));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price input. Please enter a valid number.");
                }
            }

            // Update room using RoomService
            roomService.updateRoom(room);
            System.out.println("Room updated successfully.");
        } else {
            System.out.println("Room with ID " + id + " not found.");
        }
    }

    private void deleteRoom() {
        System.out.print("Enter the ID of the room you want to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Delete room using RoomService
        boolean success = roomService.deleteRoom(roomService.getById(id));
        if (success) {
            System.out.println("Room with ID " + id + " has been deleted successfully.");
        } else {
            System.out.println("Room with ID " + id + " not found.");
        }
    }

    private void viewAllRooms() {
        // Retrieve all rooms using RoomService
        List<Room> rooms = roomService.getAll();
        System.out.println("All Rooms:");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}
