package Repos.Interfaces;

import Models.Product;

import java.util.ArrayList;
import java.util.List;

public interface IProductRepository {
    List<Product> getAllProducts();

    ArrayList<Product> getAllProductsUnder5Euros();

    ArrayList<Product> getExpiringProducts(int days);
}
