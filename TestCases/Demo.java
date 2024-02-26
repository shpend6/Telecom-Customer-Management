package TestCases;

import Database.DatabaseConnection;
import Enums.*;
import Models.Contact;
import Models.Customer;
import Repos.ContactRepository;
import Repos.CustomerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Demo {

    public static void main(String[] args) throws SQLException, ParseException {
        DatabaseConnection connection = new DatabaseConnection(); // Assuming you have this class to manage database connection
        CustomerRepository customerRepository = new CustomerRepository(connection.getConnection());

        DateFormat formatter = new SimpleDateFormat("yyyy-dd-mm");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Press [0] Exit, [1] Add Customer, [2] Update Customer, [3] Delete Customer, [4] View All Customers, [5] View a Customer");
        int input = scanner.nextInt();
        if(input == 0) {
            return;
        }

        if (input == 1) {
            System.out.println("Enter Customer Type (INDIVIDUAL or BUSINESS): ");
            scanner.nextLine(); // Move to the next line to correctly capture the full input
            String type = scanner.nextLine().trim(); // This ensures we capture the full input for the type
            CustomerEnum customerType = CustomerEnum.valueOf(type);

            System.out.println("Enter Customer Details");

            IdEnum idType = IdEnum.CU;

            System.out.print("First Name: ");
            String name = scanner.nextLine();

            System.out.print("Last Name: ");
            String lastname = scanner.nextLine();

            String customerName = null;
            if(customerType == CustomerEnum.BUSINESS) {
                System.out.println("Customer Name: ");
                customerName = scanner.nextLine();
            }

            System.out.print("Gender (M or F): ");
            GenderEnum gender = GenderEnum.valueOf(scanner.nextLine());

            System.out.print("Birthday (yyyy-dd-mm): ");
            String dobString = scanner.nextLine();
            Date dob = formatter.parse(dobString);

            Date current = new Date();

            StateEnum state = StateEnum.ACTIVE;

            Contact contact = new Contact(idType, name, lastname, customerName, gender, dob, current, state);
            Customer customer = new Customer(customerType, current, state);

            customerRepository.add(customer, contact);
            System.out.println("Customer " + contact.getName() + " was added successfully!");

            return;
        }
        if (input == 2) {
            System.out.println("Enter Customer's ID You Wish To Update:");
            int customerId = scanner.nextInt();

            scanner.nextLine();

            System.out.println("Leave Black Attributes You Wish To Leave Unchanged!");
            System.out.println("Enter Customer Type (INDIVIDUAL or BUSINESS): ");
            CustomerEnum customerType = CustomerEnum.valueOf(scanner.nextLine());

            Date current = new Date();

            System.out.print("State (ACTIVE, INACTIVE, DEACTIVE): ");
            StateEnum state = StateEnum.valueOf(scanner.nextLine());

            Customer customer = new Customer(customerType, current, state);
            customer.setId(customerId);

            customerRepository.update(customer);

            return;
        }
        if (input == 3) {
            System.out.println("Enter Customer's ID You Wish To Delete:");
            int customerId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Are You Sure You Wish To Delete Customer ID: " + customerId + "? (y or n)");
            String confirm = scanner.nextLine().trim();

            if(confirm.equals("y")) {
                customerRepository.delete(customerId);
            }
            else if(confirm.equals("n")) {
                return;
            }
            else {
                throw new IllegalArgumentException("Wrong Input!");
            }

            return;
        }
        if(input == 4) {
            ArrayList<Customer> customers = customerRepository.findAll();

            for (Customer customer : customers) {
                System.out.println("ID: " + customer.getId() +
                                   ", Contact ID: " + customer.getContactId() +
                                   ", Type: " + customer.getCustomerType() +
                                   ", Created Date: " + customer.getCreatedDate() +
                                   ", State: " + customer.getState());
            }

            return;
        }
        if(input == 5) {
            System.out.println("Enter Customer ID You Wish To Find:");
            int customerId = scanner.nextInt();

            Customer customer = customerRepository.findById(customerId);

            if (customer != null) {
                System.out.println("ID: " + customer.getId());
                System.out.println("Contact ID: " + customer.getContactId());
                System.out.println("Customer Type: " + customer.getCustomerType());
                System.out.println("Created Date: " + customer.getCreatedDate());
                System.out.println("State: " + customer.getState());
            } else {
                System.out.println("Customer not found.");
            }

            return;
        }

        scanner.close();
    }
}
