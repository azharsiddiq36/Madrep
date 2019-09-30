package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

public class Obat {

    @SerializedName("obat_id")
    private String obatId;
    @SerializedName("obat_nama")
    private String obatNama;
    @SerializedName("obat_rincian")
    private String obatRincian;
    @SerializedName("obat_foto")
    private String obatFoto;
    @SerializedName("obat_id_kategori")
    private String obatIdKategori;
    @SerializedName("kategori_id")
    private String kategoriId;
    @SerializedName("kategori_nama")
    private String kategoriNama;

    public String getObatRincian() {
        return obatRincian;
    }

    public void setObatRincian(String obatRincian) {
        this.obatRincian = obatRincian;
    }

    public String getObatFoto() {
        return obatFoto;
    }

    public void setObatFoto(String obatFoto) {
        this.obatFoto = obatFoto;
    }
    public String getObatId() {
        return obatId;
    }

    public void setObatId(String obatId) {
        this.obatId = obatId;
    }

    public String getObatNama() {
        return obatNama;
    }

    public void setObatNama(String obatNama) {
        this.obatNama = obatNama;
    }

    public String getObatIdKategori() {
        return obatIdKategori;
    }

    public void setObatIdKategori(String obatIdKategori) {
        this.obatIdKategori = obatIdKategori;
    }

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
