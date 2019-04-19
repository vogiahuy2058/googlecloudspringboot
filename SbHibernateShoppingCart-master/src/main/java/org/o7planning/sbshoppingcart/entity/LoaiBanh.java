package org.o7planning.sbshoppingcart.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "LoaiBanhs")
public class LoaiBanh implements Serializable {

    private static final long serialVersionUID = -100011907814725678L;

    @Id
    @Column(name = "LoaiBanhId", length = 30, nullable = false)
    private String code;

    @Column(name = "TenLoaiBah", length = 30, nullable = false)
    private String name;

    @Lob
    @Column(name = "Photo", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;

    @OneToMany
    @JoinColumn(name ="LoaiBanhId")
    private List<Banh> banhs = new ArrayList<>();

    public LoaiBanh() {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<Banh> getBanhs() {return banhs;}

}


