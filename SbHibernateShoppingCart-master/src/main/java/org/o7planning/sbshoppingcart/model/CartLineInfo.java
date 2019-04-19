package org.o7planning.sbshoppingcart.model;
  
 
public class CartLineInfo {
  
    private BanhInfo banhInfo;
    private int quantity;
  
    public CartLineInfo() {
        this.quantity = 0;
    }
  
    public BanhInfo getBanhInfo() {
        return banhInfo;
    }
  
    public void setBanhInfo(BanhInfo banhInfo) {
        this.banhInfo = banhInfo;
    }
  
    public int getQuantity() {
        return quantity;
    }
  
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
  
    public double getAmount() {
        return this.banhInfo.getPrice() * this.quantity;
    }
     
}