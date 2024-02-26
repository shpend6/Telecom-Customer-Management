package Repos;

import Enums.StateEnum;
import Models.Contact;
import Models.Subscription;
import Repos.Interfaces.ISubscriptionRepository;

import java.sql.*;
import java.util.ArrayList;

import static Repos.ContactRepository.setPreparedStatementForContact;
import static Repos.ContactRepository.setPreparedStatementForContactPartial;

public class SubscriptionRepository implements ISubscriptionRepository {
    private Connection connection;

    public SubscriptionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Subscription findById(int id) {
        String sql = "SELECT * FROM Subscription WHERE subs_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractSubscriptionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Subscription> findAll() {
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT * FROM Subscription";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                subscriptions.add(extractSubscriptionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }

    @Override
    public void add(Subscription subscription, Contact contact) {
        int contactId = 0;

        String contactSql = "INSERT INTO Contact (id_type, name, created_date, state) VALUES (?::id_type, ?, ?, ?::state) RETURNING id";
        try (PreparedStatement contactStmt = connection.prepareStatement(contactSql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementForContactPartial(contactStmt, contact);
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

        String subSql = "INSERT INTO Subscription (contact_id, contract_id, phone_number, created_date, state) VALUES (?, ?, ?, ?, ?::state)";
        try (PreparedStatement stmt = connection.prepareStatement(subSql)) {
            stmt.setInt(1, contactId);
            stmt.setInt(2, subscription.getContractId());
            stmt.setString(3, subscription.getPhoneNumber());
            stmt.setDate(4, new Date (subscription.getCreatedDate().getTime()));
            stmt.setString(5, subscription.getState().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Subscription subscription) {
        String sql = "UPDATE Subscription SET contract_id = ?, phone_number = ?, created_date = ?, state = ?::state WHERE subs_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subscription.getContractId());
            stmt.setString(2, subscription.getPhoneNumber());
            stmt.setDate(3, new Date (subscription.getCreatedDate().getTime()));
            stmt.setString(4, subscription.getState().name());
            stmt.setInt(5, subscription.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Subscription WHERE subs_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static Subscription extractSubscriptionFromResultSet(ResultSet rs) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setContractId(rs.getInt("contract_id"));
        subscription.setPhoneNumber(rs.getString("phone_number"));
        subscription.setCreatedDate(rs.getDate("created_date"));
        subscription.setState(StateEnum.valueOf(rs.getString("state")).name());
        return subscription;
    }
}
