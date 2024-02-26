package Repos;

import Enums.CustomerEnum;
import Enums.StateEnum;
import Models.Contact;
import Models.Customer;
import Repos.Interfaces.ICustomerRepository;

import java.sql.*;
import java.util.ArrayList;

import static Repos.ContactRepository.setPreparedStatementForContact;

public class CustomerRepository implements ICustomerRepository {
    private Connection connection;

    public CustomerRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Customer findById(int id) {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Customer> findAll() {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void add(Customer customer, Contact contact) {
        int contactId = 0;

        // First, insert the contact
        String contactSql = "INSERT INTO Contact (id_type, name, lastname, customername, gender, dob, created_date, state) VALUES (?::id_type, ?, ?, ?, ?::gender, ?, ?, ?::state) RETURNING id";
        try (PreparedStatement contactStmt = connection.prepareStatement(contactSql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementForContact(contactStmt, contact);
            contactStmt.executeUpdate();

            try (ResultSet generatedKeys = contactStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contactId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String customerSql = "INSERT INTO Customer (contact_id, customer_type, created_date, state) VALUES (?, ?::customer_type, ?, ?::state)";
        try (PreparedStatement customerStmt = connection.prepareStatement(customerSql)) {
            customerStmt.setInt(1, contactId);
            customerStmt.setString(2, customer.getCustomerType().name());
            customerStmt.setDate(3, new Date(customer.getCreatedDate().getTime()));
            customerStmt.setString(4, customer.getState().name());
            customerStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE Customer SET customer_type = ?::customer_type, created_date = ?, state = ?::state WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerType().name());
            stmt.setDate(2, new java.sql.Date(customer.getCreatedDate().getTime()));
            stmt.setString(3, customer.getState().name());
            stmt.setInt(4, customer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setContactId(rs.getInt("contact_id"));
        customer.setCustomerType(CustomerEnum.valueOf(rs.getString("customer_type")).name());
        customer.setCreatedDate(rs.getDate("created_date"));
        customer.setState(StateEnum.valueOf(rs.getString("state")).name());
        return customer;
    }
}
