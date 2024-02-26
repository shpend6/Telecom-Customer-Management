package Models;

import Enums.*;
import java.util.Date;

public class Customer {
    private int id;
    private int contactId;
    private CustomerEnum customerType; // INDIVIDUAL or BUSINESS
    private Date createdDate;
    private StateEnum state; // ACTIVE, INACTIVE, DEACTIVE

    // Default Constructor
    public Customer() {
        this.id = 0;
        this.contactId = 0;
        this.customerType = null;
        this.createdDate = new Date();
        this.state = StateEnum.ACTIVE;
    }

    // Adding Customer Constructor
    public Customer(CustomerEnum customerType, Date createdDate, StateEnum state) {
        this.customerType = customerType;
        this.createdDate = createdDate;
        this.state = state;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getContactId() {
        return contactId;
    }

    public CustomerEnum getCustomerType() {
        return customerType;
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

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setCustomerType(String customerType) {
        this.customerType = CustomerEnum.valueOf(customerType);
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setState(String state) {
        this.state = StateEnum.valueOf(state);
    }

    @Override
    public String toString() {
        return "id=" + id +
                " | contact_id=" + contactId +
                " | customer_type=" + customerType +
                " | created_date=" + createdDate +
                " | state=" + state;
    }
}
