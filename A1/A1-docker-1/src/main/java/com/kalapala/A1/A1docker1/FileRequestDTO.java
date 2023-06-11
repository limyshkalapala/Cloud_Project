package com.kalapala.A1.A1docker1;

import org.springframework.stereotype.Component;

@Component
public class FileRequestDTO {
        String file;
        String product;

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
