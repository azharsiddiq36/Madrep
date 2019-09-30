package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

public class Kategori {
    @SerializedName("kategori_id")
    private String kategoriId;
    @SerializedName("kategori_nama")
    private String kategoriNama;

    public String getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(String kategoriId) {
        this.kategoriId = kategoriId;
    }

    public String getKategoriNama() {
        return kategoriNama;
    }

    public void setKategoriNama(String kategoriNama) {
        this.kategoriNama = kategoriNama;
    }
}
