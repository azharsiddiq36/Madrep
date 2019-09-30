package com.azhar.madrep.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseAbsensi {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<Absensi> data;

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

    public ArrayList<Absensi> getData() {
        return data;
    }

    public void setData(ArrayList<Absensi> data) {
        this.data = data;
    }
}
