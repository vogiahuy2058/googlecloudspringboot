package org.o7planning.sbshoppingcart.form;

import org.o7planning.sbshoppingcart.entity.Banh;
import org.springframework.web.multipart.MultipartFile;

public class BanhForm {
    private String code;
    private String name;
    private double price;
    private String loaiBanh;

    private boolean newBanh = false;

    // Upload file.
    private MultipartFile fileData;

    public BanhForm() {
        this.newBanh= true;
    }

    public BanhForm(Banh banh) {
        this.code = banh.getCode();
        this.name = banh.getName();
        this.price = banh.getPrice();
        this.loaiBanh = banh.getLoaiBanh().getName();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    public boolean isNewBanh() {
        return newBanh;
    }

    public void setNewBanh(boolean newProduct) {
        this.newBanh = newProduct;
    }

    public String getLoaiBanh() {
        return loaiBanh;
    }

    public void setLoaiBanh(String loaiBanh) {
        this.loaiBanh = loaiBanh;
    }

}