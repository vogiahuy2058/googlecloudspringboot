package org.o7planning.sbshoppingcart.model;

import org.o7planning.sbshoppingcart.entity.LoaiBanh;

import java.util.List;

public class LoaiBanhInfo {
    private String code;
    private String name;

    private List<BanhInfo> banhs;
    public LoaiBanhInfo() {
    }

    public LoaiBanhInfo(LoaiBanh loaiBanh) {
        this.code = loaiBanh.getCode();
        this.name = loaiBanh.getName();
    }

    // Using in JPA/Hibernate query
    public LoaiBanhInfo(String code, String name) {
        this.code = code;
        this.name = name;
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

    public List<BanhInfo> getBanhs() {
        return banhs;
    }

    public void setBanhs(List<BanhInfo> banhs) {
        this.banhs = banhs;
    }
    
}