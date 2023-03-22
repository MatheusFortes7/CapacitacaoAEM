package br.com.compass.capacitacao.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class Note {

    private int number;
    private int idProduct;
    private int idClient;
    private float price;


    public Note() {}

    public Note(int number, int idProduct, int idClient, float price) {
        super();
        this.number = number;
        this.idProduct = idProduct;
        this.idClient = idClient;
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return("Number: " + number + " IdProduct: " + idProduct + " IdClient: " + idClient + " Price: " + price);
    }
}
