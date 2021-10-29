package com.example.store.ui.dashboard;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Cart_list {

    @SerializedName("user_list") //object name
    private ArrayList<Cart_model> FileList;

    public ArrayList<Cart_model> getUserList() {
        return FileList;
    }

    public void setFileList(ArrayList<Cart_model> userList) {
        FileList = userList;
    }
}
