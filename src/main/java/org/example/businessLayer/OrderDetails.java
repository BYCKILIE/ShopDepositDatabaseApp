package org.example.businessLayer;

import org.example.DAO.BillDao;
import org.example.DAO.ClientDAO;
import org.example.DAO.ProductDAO;
import org.example.model.Bill;
import org.example.model.Client;
import org.example.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDetails {
    ArrayList<String[]> clientData = new ArrayList<>();
    ArrayList<String[]> productData = new ArrayList<>();
    ClientDAO client = new ClientDAO();
    ProductDAO product = new ProductDAO();
    BillDao bill = new BillDao();

    public OrderDetails() {
        List<Client> data1 = client.findAll();
        if (data1 != null) {
            for (Client datum : data1) {
                String[] temp = new String[2];
                temp[0] = String.valueOf(datum.getId());
                temp[1] = datum.getName();
                clientData.add(temp);
            }
        }

        List<Product> data2 = product.findAll();
        if (data2 != null) {
            for (Product datum : data2) {
                String[] temp = new String[4];
                temp[0] = String.valueOf(datum.getId());
                temp[1] = datum.getName();
                temp[2] = String.valueOf(datum.getQuantity());
                temp[3] = String.valueOf(datum.getPrice());
                productData.add(temp);
            }
        }
    }

    public String[] getDisplayData(String category) {
        if (category.equals("client")) {
            String[] res = new String[clientData.size()];
            for (int i = 0; i < clientData.size(); i++) {
                res[i] = clientData.get(i)[0] + " - " + clientData.get(i)[1];
            }
            return res;
        } else if (category.equals("product")) {
            String[] res = new String[productData.size()];
            for (int i = 0; i < productData.size(); i++) {
                String temp = productData.get(i)[0] + " - " + productData.get(i)[1] +
                        " - " + productData.get(i)[2] + " - " + productData.get(i)[3];
                res[i] = temp;
            }
            return res;
        }
        return null;
    }

    public String[] createBill(ArrayList<String> data) {
        int quantity = Integer.parseInt(data.get(0));
        int idClient = Integer.parseInt(data.get(1)), idProduct = Integer.parseInt(data.get(2));

        Product productVerify = product.findById(idProduct);
        if (productVerify.getQuantity() < quantity)
            return null;

        String[] updatedProduct = new String[]{"", String.valueOf(productVerify.getQuantity() - quantity), ""};
        product.update(idProduct, updatedProduct);

        int totalPrice = productVerify.getPrice() * quantity;

        String[] billData =  new String[]{String.valueOf(totalPrice)};
        bill.insert(billData);

        return new String[]{String.valueOf(totalPrice), String.valueOf(quantity),
                String.valueOf(idClient), String.valueOf(idProduct)};
    }

}
