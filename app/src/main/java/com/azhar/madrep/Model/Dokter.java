package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

public class Dokter {

    @SerializedName("dokter_id")
    private String dokterId;
    @SerializedName("dokter_id_pengguna")
    private String dokterIdPengguna;
    @SerializedName("dokter_nama")
    private String dokterNama;
    @SerializedName("dokter_alamat")
    private String dokterAlamat;
    @SerializedName("dokter_jk")
    private String dokterJk;
    @SerializedName("dokter_bidang")
    private String dokterBidang;
    @SerializedName("dokter_nomor")
    private String dokterNomor;

    public String getDokterFoto() {
        return dokterFoto;
    }

    public void setDokterFoto(String dokterFoto) {
        this.dokterFoto = dokterFoto;
    }

    @SerializedName("dokter_foto")
    private String dokterFoto;
    @SerializedName("dokter_tgl_lahir")
    private String dokterTglLahir;

    public String getDokterId() {
        return dokterId;
    }

    public void setDokterId(String dokterId) {
        this.dokterId = dokterId;
    }

    public String getDokterIdPengguna() {
        return dokterIdPengguna;
    }

    public void setDokterIdPengguna(String dokterIdPengguna) {
        this.dokterIdPengguna = dokterIdPengguna;
    }

    public String getDokterNama() {
        return dokterNama;
    }

    public void setDokterNama(String dokterNama) {
        this.dokterNama = dokterNama;
    }

    public String getDokterAlamat() {
        return dokterAlamat;
    }

    public void setDokterAlamat(String dokterAlamat) {
        this.dokterAlamat = dokterAlamat;
    }

    public String getDokterJk() {
        return dokterJk;
    }

    public void setDokterJk(String dokterJk) {
        this.dokterJk = dokterJk;
    }

    public String getDokterBidang() {
        return dokterBidang;
    }

    public void setDokterBidang(String dokterBidang) {
        this.dokterBidang = dokterBidang;
    }

    public String getDokterNomor() {
        return dokterNomor;
    }

    public void setDokterNomor(String dokterNomor) {
        this.dokterNomor = dokterNomor;
    }

    public String getDokterTglLahir() {
        return dokterTglLahir;
    }

    public void setDokterTglLahir(String dokterTglLahir) {
        this.dokterTglLahir = dokterTglLahir;
    }
}
