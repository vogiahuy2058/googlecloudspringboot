package org.o7planning.sbshoppingcart.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Banhs")
public class Banh implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BanhId", length = 30, nullable = false)
    private String code;

    @Column(name = "TenBanh", length = 30, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LoaiBanhId", nullable = true, //
            foreignKey = @ForeignKey(name = "BANH_LOAIBANH_FK"))
    private LoaiBanh loaiBanh;

    @Column(name = "DonGia", nullable = false)
    private double price;

    @Lob
    @Column(name = "PhoTo", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;


    public Banh() {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LoaiBanh getLoaiBanh()
    {
        return loaiBanh;
    }

    public void setLoaiBanh(LoaiBanh loaiBanh)
    {
        this.loaiBanh = loaiBanh;
    }
}