package Repos.Interfaces;

import Models.Contact;
import Models.Contract;
import Models.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IContractRepository {
    Contract findById(int id);
    ArrayList<Contract> findAll();
    void add(Contract contract, Contact contact, Contact customer) throws SQLException;
    void update(Contract contract);
    void delete(int id);
}
