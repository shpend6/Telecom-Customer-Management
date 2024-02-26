package TestCases;

import Controllers.*;
import Database.DatabaseConnection;
import Exceptions.*;
import Repos.*;
import Enums.*;
import Models.*;
import Repos.Interfaces.IContactRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ContactDemo {
    public static void main(String[] args) throws SQLException, ParseException {
        DatabaseConnection connection = new DatabaseConnection(); // Assuming you have this class to manage database connection
        ContactRepository contactRepository = new ContactRepository(connection.getConnection());
        // Input scanner
        Scanner scanner = new Scanner(System.in);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("What would you like to do? Press: [0] Exit; [1]Add a contact; [2]Update a contact; [3]Delete a contact; [4]Find a contact; [5]Show all contacts");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if(choice == 0) {
            return;
        }
        if (choice == 1) {
            System.out.println("Enter Contact Details");

            System.out.print("ID Type (CU, CO, SU): ");
            IdEnum idType = IdEnum.valueOf(scanner.nextLine());

            String name = null;
            String lastname = null;
            String customerName = null;
            Date dob = new Date();
            GenderEnum gender = null;

            if (idType == IdEnum.CU) {
                System.out.print("Customer Type (INDIVIDUAL, BUSINESS): ");
                CustomerEnum customerType = CustomerEnum.valueOf(scanner.nextLine());


                if (customerType == CustomerEnum.INDIVIDUAL) {
                    System.out.print("Name: ");
                    name = scanner.nextLine();

                    System.out.print("Lastname: ");
                    lastname = scanner.nextLine();

                    System.out.print("Birthday (yyyy-MM-dd): ");
                    String dobString = scanner.nextLine();
                    dob = formatter.parse(dobString);

                    System.out.print("Gender (M, F): ");
                    gender = GenderEnum.valueOf(scanner.nextLine());
                }

                if (customerType == CustomerEnum.BUSINESS) {
                    System.out.print("Customer name: ");
                    customerName = scanner.nextLine();
                }
            }
            if (idType == IdEnum.CO || idType == IdEnum.SU) {
                System.out.print("Name: ");
                name = scanner.nextLine();
            }

            System.out.print("CURRENT DATE (yyyy-MM-dd): ");
            String current_date = scanner.nextLine();
            Date current = formatter.parse(current_date);

            System.out.print("State (ACTIVE): ");
            StateEnum state = StateEnum.valueOf(scanner.nextLine());

            Contact contact = new Contact(idType, name, lastname, customerName, gender, dob, current, state);

            contactRepository.add(contact);

            System.out.println("Contact added successfully!");

        }
        if (choice == 2) {
            System.out.println("Enter Contact ID to update:");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Fetch the id_type for the given contact's id
            IdEnum idType = contactRepository.fetchIdTypeForContactId(id); // This method needs to be implemented

            // Assuming fetchIdTypeForContactId returns null if no contact found
            if (idType == null) {
                System.out.println("No contact found with the given ID.");
                return; // Exit the current operation
            }

            // Collecting updated contact details
            System.out.println("Leave Blank Attributes You Wish To Leave Unchanged!");
            System.out.println("Enter Updated Contact Details");

            System.out.print("Name: ");
            String name = scanner.nextLine();

            Contact contactToUpdate;
            Date createdDate = new Date(); // Assuming createdDate is set to current date for update purposes

            if (idType == IdEnum.CU) {
                System.out.print("Lastname: ");
                String lastname = scanner.nextLine();

                System.out.print("Birthday (yyyy-mm-dd): ");
                String dobString = scanner.nextLine();
                Date dob = formatter.parse(dobString); // Ensure formatter is defined earlier

                System.out.print("Customer name: ");
                String customerName = scanner.nextLine();

                System.out.print("Gender (M, F): ");
                GenderEnum gender = GenderEnum.valueOf(scanner.nextLine().toUpperCase());

                System.out.print("State (ACTIVE, ...): ");
                StateEnum state = StateEnum.valueOf(scanner.nextLine().toUpperCase());

                // Using the constructor for Customer
                contactToUpdate = new Contact(idType, name, lastname, customerName, gender, dob, createdDate, state);
                contactToUpdate.setId(id);
            } else if (idType == IdEnum.CO || idType == IdEnum.SU) {
                System.out.print("State (ACTIVE, ...): ");
                StateEnum state = StateEnum.valueOf(scanner.nextLine().toUpperCase());

                // Using the constructor for Contract and Subscription
                contactToUpdate = new Contact(idType, name, createdDate, state);
                contactToUpdate.setId(id);
            } else {
                System.out.println("Invalid ID Type.");
                return; // Exit the current operation
            }

            // Now, update the contact in the database
            contactRepository.update(contactToUpdate);

            System.out.println("Contact updated successfully!");
        }
        if (choice == 3) {
            System.out.println("Enter Contact ID to delete:");
            int id = scanner.nextInt();
            scanner.nextLine();

            contactRepository.delete(id);

            System.out.println("Contact deleted successfully!");
        }
        if (choice == 4) {
            System.out.println("Enter Contact ID to find:");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            Contact contact = contactRepository.findById(id);
            if (contact != null) {
                System.out.println("Contact found:");
                System.out.println("ID: " + contact.getId() +
                        ", ID Type: " + contact.getIdType() +
                        ", Name: " + contact.getName() +
                        ", Last Name: " + contact.getLastName() +
                        ", Customer Name: " + contact.getCustomerName() +
                        ", Gender: " + contact.getGender() +
                        ", DOB: " + contact.getDob() +
                        ", Created Date: " + contact.getCreatedDate() +
                        ", State: " + contact.getState());
            }  else {
                System.out.println("Contact with ID " + id + " not found.");
            }
        }
        if (choice == 5) {
            ArrayList<Contact> allContacts = contactRepository.findAll();
            if (!allContacts.isEmpty()) {
                System.out.println("All Contacts:");
                for (Contact contact : allContacts) {
                    System.out.println("ID: " + contact.getId() +
                            ", ID Type: " + contact.getIdType() +
                            ", Name: " + contact.getName() +
                            ", Last Name: " + contact.getLastName() +
                            ", Customer Name: " + contact.getCustomerName() +
                            ", Gender: " + contact.getGender() +
                            ", DOB: " + contact.getDob() +
                            ", Created Date: " + contact.getCreatedDate() +
                            ", State: " + contact.getState());
                }
            } else {
                System.out.println("No contacts found.");
            }
        }
    }
}

