package org.o7planning.sbshoppingcart.form;

import org.o7planning.sbshoppingcart.entity.LoaiBanh;
import org.springframework.web.multipart.MultipartFile;

public class LoaiBanhForm {
    private String code;
    private String name;
    private boolean newLoaiBanh = false;

    // Upload file.
    private MultipartFile fileData;

    public LoaiBanhForm() {
        this.newLoaiBanh= true;
    }

    public LoaiBanhForm(LoaiBanh loaiBanh) {
        this.code = loaiBanh.getCode();
        this.name = loaiBanh.getName();
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

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }


    public boolean isNewLoaiBanh() {
        return newLoaiBanh;
    }

    public void setNewLoaiBanh(boolean newLoaiBanh) {
        this.newLoaiBanh = newLoaiBanh;
    }

}
