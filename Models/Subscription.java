package Models;

import Enums.*;
import java.util.Date;

public class Subscription {
    private int id;
    private int contact_id;
    private int contract_id;
    private String phoneNumber;
    private Date createdDate;
    private StateEnum state;

    // Constructor
    public Subscription(int contact_id, int contract_id, String phoneNumber, Date createdDate, StateEnum state) {
        this.contact_id = contact_id;
        this.contract_id = contract_id;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
        this.state = state;
    }

    // Default Constructor
    public Subscription() {
        this.contact_id = 0;
        this.contract_id = 0;
        this.phoneNumber = "+383 44 000 000";
        this.createdDate = new Date();
        this.state = StateEnum.ACTIVE;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getContactId() {
        return contact_id;
    }

    public int getContractId() {
        return contract_id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public StateEnum getState() {
        return state;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setContractId(int contract_id) {
        this.contract_id = contract_id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setState(String state) {
        this.state = StateEnum.valueOf(state);
    }
}
