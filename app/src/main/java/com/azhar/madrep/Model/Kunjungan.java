package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

public class Kunjungan {

    @SerializedName("kunjungan_id")
    private String kunjunganId;
    @SerializedName("kunjungan_id_madrep")
    private String kunjunganIdMadrep;
    @SerializedName("kunjungan_id_dokter")
    private String kunjunganIdDokter;
    @SerializedName("kunjungan_cabang")
    private String kunjunganCabang;
    @SerializedName("kunjungan_bulan")
    private String kunjunganBulan;
    @SerializedName("kunjungan_minggu")
    private String kunjunganMinggu;
    @SerializedName("kunjungan_latitude")
    private String kunjunganLatitude;
    @SerializedName("kunjungan_longitude")
    private String kunjunganLongitude;
    @SerializedName("kunjungan_keterangan")
    private String kunjunganKeterangan;
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
    @SerializedName("dokter_tgl_lahir")
    private String dokterTglLahir;
    @SerializedName("dokter_foto")
    private String dokterFoto;
    @SerializedName("kunjungan_obat")
    private String kunjungan_obat;

    public String getKunjungan_obat() {
        return kunjungan_obat;
    }

    public void setKunjungan_obat(String kunjungan_obat) {
        this.kunjungan_obat = kunjungan_obat;
    }

    public String getKunjunganId() {
        return kunjunganId;
    }

    public void setKunjunganId(String kunjunganId) {
        this.kunjunganId = kunjunganId;
    }

    public String getKunjunganIdMadrep() {
        return kunjunganIdMadrep;
    }

    public void setKunjunganIdMadrep(String kunjunganIdMadrep) {
        this.kunjunganIdMadrep = kunjunganIdMadrep;
    }

    public String getKunjunganIdDokter() {
        return kunjunganIdDokter;
    }

    public void setKunjunganIdDokter(String kunjunganIdDokter) {
        this.kunjunganIdDokter = kunjunganIdDokter;
    }

    public String getKunjunganCabang() {
        return kunjunganCabang;
    }

    public void setKunjunganCabang(String kunjunganCabang) {
        this.kunjunganCabang = kunjunganCabang;
    }

    public String getKunjunganBulan() {
        return kunjunganBulan;
    }

    public void setKunjunganBulan(String kunjunganBulan) {
        this.kunjunganBulan = kunjunganBulan;
    }

    public String getKunjunganMinggu() {
        return kunjunganMinggu;
    }

    public void setKunjunganMinggu(String kunjunganMinggu) {
        this.kunjunganMinggu = kunjunganMinggu;
    }

    public String getKunjunganLatitude() {
        return kunjunganLatitude;
    }

    public void setKunjunganLatitude(String kunjunganLatitude) {
        this.kunjunganLatitude = kunjunganLatitude;
    }

    public String getKunjunganLongitude() {
        return kunjunganLongitude;
    }

    public void setKunjunganLongitude(String kunjunganLongitude) {
        this.kunjunganLongitude = kunjunganLongitude;
    }

    public String getKunjunganKeterangan() {
        return kunjunganKeterangan;
    }

    public void setKunjunganKeterangan(String kunjunganKeterangan) {
        this.kunjunganKeterangan = kunjunganKeterangan;
    }

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

    public String getDokterFoto() {
        return dokterFoto;
    }

    public void setDokterFoto(String dokterFoto) {
        this.dokterFoto = dokterFoto;
    }
}
