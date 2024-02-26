package Repos;

import Models.*;
import Repos.Interfaces.*;

import java.sql.*;
import java.util.ArrayList;

import static Repos.ContactRepository.setPreparedStatementForContactPartial;

public class ContractRepository implements IContractRepository {
    private Connection connection;

    public ContractRepository(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Contract findById(int id) {
        String sql = "SELECT * FROM contracts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractContractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Contract> findAll() {
        ArrayList<Contract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM contracts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                contracts.add(extractContractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contracts;
    }

    @Override
    public void add(Contract contract, Contact contact, Contact customer) throws SQLException {
        int contactId = 0;

        // First, insert the contact
        String contactSql = "INSERT INTO Contact (id_type, name, created_date, state) VALUES (?::id_type, ?, ?, ?::state) RETURNING id";
        try (PreparedStatement contactStmt = connection.prepareStatement(contactSql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementForContactPartial(contactStmt, contact);
            contactStmt.executeUpdate();

            try (ResultSet generatedKeys = contactStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contactId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating contact failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //insert the customer and associate it with the contact
        int customerId = selectCustomerIdByNameAndLastName(customer.getName(), customer.getLastName());
        if(customerId == 0) {
            throw new SQLException("Customer not found.");
        }

        // insert the contract and associate it with the customer
        String contractSql = "INSERT INTO Contract (contact_id, customer_id, contract_type, created_date, state) VALUES (?, ?, ?::contract_type, ?, ?::state)";
        try (PreparedStatement contractStmt = connection.prepareStatement(contractSql)) {
            contractStmt.setInt(1, contactId);
            contractStmt.setInt(2, customerId);
            contractStmt.setString(3, contract.getContractType().name());
            contractStmt.setDate(4, new Date(contract.getCreatedDate().getTime()));
            contractStmt.setString(5, contract.getState().name());
            contractStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Contract contract) {
        String sql = "UPDATE contracts SET customer_id = ?, contract_type = ?::contract_type, created_date = ?, state = ?::state WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, contract.getCustomerId());
            pstmt.setString(2, contract.getContractType().name());
            pstmt.setTimestamp(3, new Timestamp(contract.getCreatedDate().getTime()));
            pstmt.setString(4, contract.getState().name());
            pstmt.setInt(5, contract.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM contracts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Contract extractContractFromResultSet(ResultSet rs) throws SQLException {
        Contract contract = new Contract();
        contract.setId(rs.getInt("id"));
        contract.setCustomerId(rs.getInt("customer_id"));
        contract.setContractType(rs.getString("contract_type"));
        contract.setCreatedDate(new Date(rs.getTimestamp("created_date").getTime()));
        contract.setState(rs.getString("state"));
        return contract;
    }

    /**
     * Selects a customer ID based on contact name and lastname.
     *
     * @param name The contact's first name.
     * @param lastname The contact's last name.
     * @return The customer ID if found, 0 otherwise.
     * @throws SQLException If there is a database access error or other errors.
     */
    private int selectCustomerIdByNameAndLastName(String name, String lastname) throws SQLException {
        String query = "SELECT id FROM Customer WHERE contact_id = (SELECT id FROM Contact WHERE id_type = 'CU' AND name = ? AND lastname = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, lastname);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to handle it outside the method
        }
        return 0; // Return 0 if customer not found
    }
}