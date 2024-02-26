package Repos;

import Enums.GenderEnum;
import Enums.IdEnum;
import Enums.StateEnum;
import Models.Contact;
import Repos.Interfaces.IContactRepository;

import java.sql.*;
import java.util.ArrayList;

public class ContactRepository implements IContactRepository {
    private Connection connection;

    public ContactRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Contact findById(int id) {
        String sql = "SELECT * FROM Contact WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractContactFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Contact> findAll() {
        ArrayList<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM Contact";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contacts.add(extractContactFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public void add(Contact contact) {
        String sql = "INSERT INTO Contact (id_type, name, lastname, customername, gender, dob, created_date, state) VALUES (?::id_type, ?, ?, ?, ?::gender, ?, ?, ?::state)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setPreparedStatementForContact(stmt, contact);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Contact contact) {
        String sql = "UPDATE Contact SET id_type = ?::id_type, name = ?, lastname = ?, customername = ?, gender = ?::gender, dob = ?, created_date = ?, state = ?::state WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setPreparedStatementForContact(stmt, contact);
            stmt.setInt(9, contact.getId()); // Assuming the ID is the last parameter for update
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Contact WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static Contact extractContactFromResultSet(ResultSet rs) throws SQLException {
        Contact contact = new Contact();

        contact.setId(rs.getInt("id"));
        contact.setIdType(IdEnum.valueOf(rs.getString("id_type")).name());
        contact.setName(rs.getString("name"));
        contact.setLastName(rs.getString("lastname"));
        contact.setCustomerName(rs.getString("customername"));
        contact.setGender(GenderEnum.valueOf(rs.getString("gender")).name());
        contact.setDob(rs.getDate("dob"));
        contact.setCreatedDate(rs.getDate("created_date"));
        contact.setState(StateEnum.valueOf(rs.getString("state")).name());

        return contact;
    }

    protected static void setPreparedStatementForContact(PreparedStatement stmt, Contact contact) throws SQLException {
        stmt.setString(1, contact.getIdType().name());
        stmt.setString(2, contact.getName());
        stmt.setString(3, contact.getLastName());
        stmt.setString(4, contact.getCustomerName());
        stmt.setString(5, contact.getGender().name());
        stmt.setDate(6, new Date(contact.getDob().getTime()));
        stmt.setDate(7, new Date(contact.getCreatedDate().getTime()));
        stmt.setString(8, contact.getState().name());
    }

    protected static void setPreparedStatementForContactPartial(PreparedStatement stmt, Contact contact) throws SQLException {
        stmt.setString(1, contact.getIdType().name());
        stmt.setString(2, contact.getName());
        stmt.setDate(3, new Date(contact.getCreatedDate().getTime()));
        stmt.setString(4, contact.getState().name());
    }

    public IdEnum fetchIdTypeForContactId(int contactId) {
        String sql = "SELECT id_type FROM contact WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, contactId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String idTypeString = resultSet.getString("id_type");
                    return IdEnum.valueOf(idTypeString);
                } else {
                    System.out.println("No contact found with ID: " + contactId);
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
