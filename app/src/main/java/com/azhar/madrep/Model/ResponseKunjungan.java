package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseKunjungan {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<Kunjungan> data;

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

    public ArrayList<Kunjungan> getData() {
        return data;
    }

    public void setData(ArrayList<Kunjungan> data) {
        this.data = data;
    }
}
