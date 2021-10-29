package com.example.store.ui.home;


import com.google.gson.annotations.SerializedName;

public class Product_model {

    @SerializedName("product_id")
    private String product_id;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("quantity")
    private int quantity;

    public Product_model(String product_id,String product_name,int quantity){
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }
}
