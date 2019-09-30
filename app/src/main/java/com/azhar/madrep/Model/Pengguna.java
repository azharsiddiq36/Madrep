package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

public class Pengguna {
    @SerializedName("pengguna_id")
    private String penggunaId;
    @SerializedName("pengguna_username")
    private String penggunaUsername;
    @SerializedName("pengguna_hak_akses")
    private String penggunaHakAkses;
    @SerializedName("pengguna_password")
    private String penggunaPassword;
    @SerializedName("pengguna_email")
    private String penggunaEmail;

    public String getPenggunaId() {
        return penggunaId;
    }

    public void setPenggunaId(String penggunaId) {
        this.penggunaId = penggunaId;
    }

    public String getPenggunaUsername() {
        return penggunaUsername;
    }

    public void setPenggunaUsername(String penggunaUsername) {
        this.penggunaUsername = penggunaUsername;
    }

    public String getPenggunaHakAkses() {
        return penggunaHakAkses;
    }

    public void setPenggunaHakAkses(String penggunaHakAkses) {
        this.penggunaHakAkses = penggunaHakAkses;
    }

    public String getPenggunaPassword() {
        return penggunaPassword;
    }

    public void setPenggunaPassword(String penggunaPassword) {
        this.penggunaPassword = penggunaPassword;
    }

    public String getPenggunaEmail() {
        return penggunaEmail;
    }

    public void setPenggunaEmail(String penggunaEmail) {
        this.penggunaEmail = penggunaEmail;
    }




}
