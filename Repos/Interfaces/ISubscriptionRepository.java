package Repos.Interfaces;

import Models.Contact;
import Models.Subscription;

import java.util.ArrayList;

public interface ISubscriptionRepository {
    Subscription findById(int id);
    ArrayList<Subscription> findAll();
    void add(Subscription subscription, Contact contact);
    void update(Subscription subscription);
    void delete(int id);
}
