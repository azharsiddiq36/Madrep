package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDokter {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<Dokter> data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Dokter> getData() {
        return data;
    }

    public void setData(ArrayList<Dokter> data) {
        this.data = data;
    }
}
