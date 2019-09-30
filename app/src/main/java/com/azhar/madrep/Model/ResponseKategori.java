package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseKategori {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<Kategori> data;

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

    public void setData(ArrayList<Kategori> data) {
        this.data = data;
    }

    public ArrayList<Kategori> getData() {
        return data;
    }
}
