package Controllers;

import Database.DatabaseConnection;
import Models.Contact;
import Models.Customer;
import Repos.Interfaces.ICustomerRepository;
import Repos.CustomerRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerController {
    private ICustomerRepository customerRepository;

    DatabaseConnection connection = new DatabaseConnection();

    public CustomerController() throws SQLException {
        this.customerRepository = new CustomerRepository(connection.getConnection());
    }

    public void addCustomer(Customer customer, Contact contact){
        customerRepository.add(customer, contact);
    };
    public ArrayList<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public void delete(int id) {
        customerRepository.delete(id);

    }
    public void update(Customer customer) {
        customerRepository.update(customer);
    }



}
