package com.hotel.controllers;

import com.hotel.dao.user.CustomerRepositoryInterface;
import com.hotel.models.Customer;
import com.hotel.services.CustomerService;

import java.util.InputMismatchException;

import java.util.Scanner;

public class CustomerController {

    private final CustomerService customerService;
    private final Scanner scanner = new Scanner(System.in);

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    public void index() {
        System.out.println("Customer list");
        customerService.getAllCustomers().forEach(System.out::println);
        while (true) {
            try {
                System.out.println("Customer Management Menu:");
                System.out.println("1. Create Customer");
                System.out.println("2. Edit Customer");
                System.out.println("3. Delete Customer");
                System.out.println("4. View Statistics");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (option) {
                    case 1:
                        createCustomer();
                        break;
                    case 2:
                        editCustomer();
                        break;
                    case 3:
                        deleteCustomer();
                        break;
                    case 4:
                        viewStatistics();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private void createCustomer() {
        System.out.println("Creating a new customer.");
        Customer customer = new Customer();
        System.out.print("Enter customer email: ");
        customer.setEmail(scanner.nextLine());
        System.out.print("Enter customer name: ");
        customer.setName(scanner.nextLine());
        System.out.print("Enter customer password: ");
        customer.setPassword(scanner.nextLine());

        customerService.addCustomer(customer);
        System.out.println("Customer created successfully.");
    }


    private void editCustomer() {
        System.out.print("Enter the ID of the customer you want to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();


        Customer customer = customerService.getCustomer(id);
        if (customer != null) {
            System.out.println("Editing customer with ID " + id);
            System.out.print("Enter new email (leave blank to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) customer.setEmail(email);

            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) customer.setName(name);

            System.out.print("Enter new password (leave blank to keep current): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) customer.setPassword(password);

            // Update customer in repository
            customerService.updateCustomer(customer);
            System.out.println("Customer updated successfully.");
        } else {
            System.out.println("Customer with ID " + id + " not found.");
        }
    }


    private void deleteCustomer() {
        System.out.print("Enter the ID of the customer you want to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean success = customerService.deleteCustomer(customerService.getCustomer(id));
        if (success) {
            System.out.println("Customer with ID " + id + " has been deleted successfully.");
        } else {
            System.out.println("Customer with ID " + id + " not found.");
        }
    }

    // Method to view customer statistics
    private void viewStatistics() {
            System.out.println("count of users:" + customerService.getCustomerCount());
    }
}
