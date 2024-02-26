package Models;

import Enums.*;
import java.util.*;

public class Contact {

    private int id;
    private IdEnum idType; // CU-Customer, CO-Contract, SU-Subscription
    private String name;
    private String lastName;
    private String customerName;
    private GenderEnum gender; // M, F
    private Date dob;
    private Date createdDate;
    private StateEnum state; // ACTIVE, INACTIVE, DEACTIVE

    // Default constructor
    public Contact() {
        this.idType = null;
        this.name = "";
        this.lastName = "";
        this.customerName = "";
        this.gender = null;
        this.dob = new Date();
        this.createdDate = new Date();
        this.state = StateEnum.ACTIVE;
    }

    // Constructor used for Customer
    public Contact(IdEnum idType, String name, String lastName, String customerName, GenderEnum gender, Date dob, Date createdDate, StateEnum state) {
        this.idType = idType;
        this.name = name;
        this.lastName = lastName;
        this.customerName = customerName;
        this.gender = gender;
        this.dob = dob;
        this.createdDate = createdDate;
        this.state = state;
    }

    // Constructor used for Contract and Subscription
    public Contact(IdEnum idType, String name, Date createdDate, StateEnum state) {
        this.idType = idType;
        this.name = name;
        this.createdDate = createdDate;
        this.state = state;
    }

    // Getters
    public int getId() {
        return id;
    }

    public IdEnum getIdType() {
        return idType;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
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

    public void setIdType(String idType) {
        this.idType = IdEnum.valueOf(idType);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public void setCustomerName(String customername) {
        this.customerName = customername;
    }

    public void setGender(String gender) {
        this.gender = GenderEnum.valueOf(gender);
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setState(String state) { this.state = StateEnum.valueOf(state); }
}
