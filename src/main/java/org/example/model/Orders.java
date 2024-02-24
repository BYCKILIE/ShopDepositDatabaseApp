package org.example.model;

public class Orders {

    private int id;
    private int total_price;
    private int quantity;
    private int id_client;
    private int id_product;

    public Orders() {}

    public Orders(int totalPrice, int quantity, int idClient, int idProduct) {
        this.total_price = totalPrice;
        this.quantity = quantity;
        this.id_client = idClient;
        this.id_product = idProduct;
    }

    public Orders(int id, int totalPrice, int quantity, int idClient, int idProduct) {
        this.id = id;
        this.total_price = totalPrice;
        this.quantity = quantity;
        this.id_client = idClient;
        this.id_product = idProduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }
}
