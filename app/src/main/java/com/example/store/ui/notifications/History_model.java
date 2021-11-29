package com.example.store.ui.notifications;


import com.google.gson.annotations.SerializedName;

public class History_model {
    @SerializedName("details")
    private String details;


    public History_model(String details){
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
