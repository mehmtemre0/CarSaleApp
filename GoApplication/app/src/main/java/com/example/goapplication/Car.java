package com.example.goapplication;

public class Car {
    private String id;
    private String title;
    private String brand;
    private String model;
    private String paket;
    private String year;
    private String hp;
    private String cc;
    private String price;
    private String ownerDetails;
    private String image;

    public Car() {
        // Default constructor required for calls to DataSnapshot.getValue(Car.class)
    }

    public Car(String id, String title, String brand, String model, String paket, String year, String hp, String cc, String price, String ownerDetails, String image) {
        this.id = id;
        this.title = title;
        this.brand = brand;
        this.model = model;
        this.paket = paket;
        this.year = year;
        this.hp = hp;
        this.cc = cc;
        this.price = price;
        this.ownerDetails = ownerDetails;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(String ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image =image;
    }
}