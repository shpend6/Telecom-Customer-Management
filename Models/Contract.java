package Models;

import Enums.*;
import java.util.*;

public class Contract {
    private int id;
    private int contact_id;
    private int customer_id;
    private ContractEnum contractType; // PREPAID, POSTPAID
    private Date createdDate;
    private StateEnum state; // ACTIVE, INACTIVE, DEACTIVE

    // Constructor
    public Contract() {
        this.contact_id = 0;
        this.customer_id = 0;
        this.contractType = ContractEnum.PREPAID;
        this.createdDate = new Date();
        this.state = StateEnum.ACTIVE;
    }

    public Contract(int contact_id, int customer_id, ContractEnum contractType, Date createdDate, StateEnum state) {
        this.contact_id = contact_id;
        this.customer_id = customer_id;
        this.contractType = contractType;
        this.createdDate = createdDate;
        this.state = state;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getContactId() {
        return contact_id;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public ContractEnum getContractType() {
        return contractType;
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

    public void setContactId(int contact_id) {
        this.contact_id = contact_id;
    }

    public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }


    public void setContractType(String contractType) {
        this.contractType = ContractEnum.valueOf(contractType);
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setState(String state) {
        this.state = StateEnum.valueOf(state);
    }
}
