package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

public class Absensi {
    @SerializedName("absensi_id")
    private String absensiId;
    @SerializedName("absensi_id_pengguna")
    private String absensiIdPengguna;
    @SerializedName("absensi_kota")
    private String absensiKota;
    @SerializedName("absensi_tanggal")
    private String absensiTanggal;
    @SerializedName("absensi_latitude")
    private String absensiLatitude;
    @SerializedName("absensi_longitude")
    private String absensiLongitude;
    @SerializedName("absensi_status")
    private String absensiStatus;
    @SerializedName("absensi_kecamatan")
    private String absensiKecamatan;
    @SerializedName("absensi_kelurahan")
    private String absensiKelurahan;
    @SerializedName("absensi_jalan")
    private String absensiJalan;

    public String getAbsensiId() {
        return absensiId;
    }

    public void setAbsensiId(String absensiId) {
        this.absensiId = absensiId;
    }

    public String getAbsensiIdPengguna() {
        return absensiIdPengguna;
    }

    public void setAbsensiIdPengguna(String absensiIdPengguna) {
        this.absensiIdPengguna = absensiIdPengguna;
    }

    public String getAbsensiKota() {
        return absensiKota;
    }

    public void setAbsensiKota(String absensiKota) {
        this.absensiKota = absensiKota;
    }

    public String getAbsensiTanggal() {
        return absensiTanggal;
    }

    public void setAbsensiTanggal(String absensiTanggal) {
        this.absensiTanggal = absensiTanggal;
    }

    public String getAbsensiLatitude() {
        return absensiLatitude;
    }

    public void setAbsensiLatitude(String absensiLatitude) {
        this.absensiLatitude = absensiLatitude;
    }

    public String getAbsensiLongitude() {
        return absensiLongitude;
    }

    public void setAbsensiLongitude(String absensiLongitude) {
        this.absensiLongitude = absensiLongitude;
    }

    public String getAbsensiStatus() {
        return absensiStatus;
    }

    public void setAbsensiStatus(String absensiStatus) {
        this.absensiStatus = absensiStatus;
    }

    public String getAbsensiKecamatan() {
        return absensiKecamatan;
    }

    public void setAbsensiKecamatan(String absensiKecamatan) {
        this.absensiKecamatan = absensiKecamatan;
    }

    public String getAbsensiKelurahan() {
        return absensiKelurahan;
    }

    public void setAbsensiKelurahan(String absensiKelurahan) {
        this.absensiKelurahan = absensiKelurahan;
    }

    public String getAbsensiJalan() {
        return absensiJalan;
    }

    public void setAbsensiJalan(String absensiJalan) {
        this.absensiJalan = absensiJalan;
    }
}
