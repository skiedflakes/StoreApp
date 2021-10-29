package com.example.store.ui.home;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Product_list {

    @SerializedName("user_list") //object name
    private ArrayList<Product_model> FileList;

    public ArrayList<Product_model> getUserList() {
        return FileList;
    }

    public void setFileList(ArrayList<Product_model> userList) {
        FileList = userList;
    }
}
