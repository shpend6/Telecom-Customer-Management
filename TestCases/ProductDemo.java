package TestCases;

import Controllers.ProductController;
import Models.Product;
import Models.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class ProductDemo {
    public static void main(String[] args) throws SQLException, ParseException {
        ProductController productController  = new ProductController();
        ArrayList<Product> products = productController.findAllProducts();
        ArrayList<Product> cheap = productController.getCheapProducts();
        ArrayList<Product> expiring = productController.getExpiringProducts(500);


        System.out.println("Te gjitha produktet: ");
        for (Product p:
             products) {
            System.out.println(p.getName());
            System.out.println("  Cmimi: " + p.getPrice());
            System.out.println("  Sherbimet:");
            for (Service s : p.getServices()) {
                System.out.println("    " + s.getServiceType().getType());
            }
        }
        System.out.println("Te gjitha produktet nen 5: ");
        for (Product p:
                cheap) {
            System.out.println(p.getName());
            System.out.println("  Cmimi: " + p.getPrice());
            System.out.println("  Sherbimet:");
            for (Service s : p.getServices()) {
                System.out.println("    " + s.getServiceType().getType());
            }
        }
        System.out.println("Te gjitha produktet qe skadojne pas 500 diteve");
        for (Product p:
                expiring) {
            System.out.println(p.getName());
            System.out.println("  Cmimi: " + p.getPrice());
            System.out.println("  Sherbimet:");
            for (Service s : p.getServices()) {
                System.out.println("    " + s.getServiceType().getType());
            }
        }

    }
}
