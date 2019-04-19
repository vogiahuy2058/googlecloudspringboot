package org.o7planning.sbshoppingcart.model;
 
public class OrderDetailInfo {
    private String id;
 
    private String banhCode;
    private String banhName;
 
    private int quanity;
    private double price;
    private double amount;
 
    public OrderDetailInfo() {
 
    }
 
    // Using for JPA/Hibernate Query.
    public OrderDetailInfo(String id, String banhCode, //
            String banhName, int quanity, double price, double amount) {
        this.id = id;
        this.banhCode = banhCode;
        this.banhName = banhName;
        this.quanity = quanity;
        this.price = price;
        this.amount = amount;
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getBanhCode() {
        return banhCode;
    }
 
    public void setBanhCode(String banhCode) {
        this.banhCode = banhCode;
    }
 
    public String getBanhName() {
        return banhName;
    }
 
    public void setBanhName(String banhName) {
        this.banhName = banhName;
    }
 
    public int getQuanity() {
        return quanity;
    }
 
    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }
 
    public double getPrice() {
        return price;
    }
 
    public void setPrice(double price) {
        this.price = price;
    }
 
    public double getAmount() {
        return amount;
    }
 
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
