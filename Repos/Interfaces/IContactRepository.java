package Repos.Interfaces;

import Models.*;
import java.util.*;

public interface IContactRepository {
    Contact findById(int id);
    ArrayList<Contact> findAll();
    void add(Contact contact);
    void update(Contact contact);
    void delete(int id);
}
