package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Services.ServiceType;

public class Product {
    private int id;
    private String name;
    private double price;
    private Date fromDateTime;
    private Date toDateTime;
    private ArrayList<Service> services; // A list of ServiceType instances

    public Product(){
//        this.id = 1;
//        this.name = "";
//        this.price = 0.0;
//        this.fromDateTime = new Date();
//        this.toDateTime = new Date();
//        this.services = new ArrayList<>();
    }
    // Constructor
    public Product(String name, double price,  Date fromDateTime, Date toDateTime, ArrayList<Service> services) {
        this.name = name;
        this.price = price;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.services = services;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(Date fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public Date getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(Date toDateTime) {
        this.toDateTime = toDateTime;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

