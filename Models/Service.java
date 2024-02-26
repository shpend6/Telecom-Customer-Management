package Models;

import java.util.Date;

import Enums.*;
import Services.ServiceType;

public class Service {
    private int id;
    private ServiceType serviceType; // Interface reference
    private Date createdDate;
    private StateEnum state; // ACTIVE, INACTIVE, DEACTIVE



    // Constructor
    public Service() {
        this.id = 1;
        this.createdDate = new Date();
        this.state = StateEnum.ACTIVE;
    }

    // Getters
    public long getId() {
        return id;
    }

    public ServiceType getServiceType() {
        return serviceType;
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

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }
}
