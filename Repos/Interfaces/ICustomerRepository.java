package Repos.Interfaces;

import Models.Contact;
import Models.Customer;
import java.util.ArrayList;

public interface ICustomerRepository {
    Customer findById(int id);
    ArrayList<Customer> findAll();
    void add(Customer customer, Contact contact);
    void update(Customer customer);
    void delete(int id);
}
