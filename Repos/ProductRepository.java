package Repos;




import Enums.CustomerEnum;
import Enums.StateEnum;
import Models.Customer;
import Models.Product;
import Models.Service;
import Repos.Interfaces.IProductRepository;
import Services.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductRepository implements IProductRepository {
    private Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }
    @Override
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";
       // try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public ArrayList<Product> getAllProductsUnder5Euros(){
        ArrayList<Product> allProducts = getAllProducts();
        ArrayList<Product> products = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getPrice() < 5.0) {
                products.add(p);
            }
        }
        return products;
    }

    @Override
    public ArrayList<Product> getExpiringProducts(int days) {
        ArrayList<Product> allProducts = getAllProducts();
        ArrayList<Product> expiringProducts = new ArrayList<>();

        // Calculate the expiration date
        Date currentDate = new Date();
        long currentTimeInMillis = currentDate.getTime();
        long expirationTimeInMillis = currentTimeInMillis + ((long) days * 24 * 60 * 60 * 1000); // Convert days to milliseconds

        for (Product p : allProducts) {
            Date toDateTime = p.getToDateTime();
            if (toDateTime != null && toDateTime.getTime() <= expirationTimeInMillis) {
                expiringProducts.add(p);
            }
        }
        return expiringProducts;

    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setFromDateTime(rs.getDate("from_date_time"));
        product.setToDateTime(rs.getDate("to_date_time"));
        product.setServices(getAllServices(product.getId()));
        return product;
    }

    private ArrayList<Service> getAllServices(int id) {
        ArrayList<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM productservice WHERE product_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                services.add(getService(rs.getInt("service_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
    private Service getService(int serviceId) {
        Service service = new Service();
        String sql = "SELECT * FROM service WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                service.setId(rs.getInt("id"));
                service.setState(StateEnum.valueOf(rs.getString("state")));
                service.setCreatedDate(rs.getDate("created_date"));
                // Retrieve the string from the database
                String serviceTypeStr = rs.getString("service_type");
                switch (serviceTypeStr) {
                    case "SMS":
                        service.setServiceType(new SMS());
                        break;
                    case "VOICE":
                        service.setServiceType(new Voice());
                        break;
                    case "SIM":
                        service.setServiceType(new SIM());
                        break;
                    case "DATA":
                        service.setServiceType(new Data());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported service type: " + serviceTypeStr);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return service;
    }
}