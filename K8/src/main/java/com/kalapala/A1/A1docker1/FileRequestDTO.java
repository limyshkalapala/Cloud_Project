package com.kalapala.A1.A1docker1;

import org.springframework.stereotype.Component;

@Component
public class FileRequestDTO {
        String file;
        String product;

        String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "FileRequestDTO{" +
                "file='" + file + '\'' +
                ", product='" + product + '\'' +
                '}';
    }
}
