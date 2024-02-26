package Controllers;

import Database.DatabaseConnection;
import Models.Product;
import Repos.ContactRepository;
import Repos.Interfaces.IContactRepository;
import Repos.Interfaces.IProductRepository;
import Repos.ProductRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private IProductRepository productRepository;

    DatabaseConnection connection = new DatabaseConnection(); // Assuming you have this class to manage database connection

    public ProductController() throws SQLException {
        this.productRepository = new ProductRepository(connection.getConnection());
    }

    public ArrayList<Product> findAllProducts() {
        return (ArrayList<Product>) productRepository.getAllProducts();
    }

    public ArrayList<Product> getCheapProducts(){
        return productRepository.getAllProductsUnder5Euros();
    }

    public ArrayList<Product> getExpiringProducts(int days){
        return productRepository.getExpiringProducts(days);
    }
}
