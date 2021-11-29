package com.example.store.ui.notifications;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class History_list {

    @SerializedName("user_list") //object name
    private ArrayList<History_model> FileList;

    public ArrayList<History_model> getUserList() {
        return FileList;
    }

    public void setFileList(ArrayList<History_model> userList) {
        FileList = userList;
    }
}
