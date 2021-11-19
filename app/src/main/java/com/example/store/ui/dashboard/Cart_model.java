package com.example.store.ui.dashboard;


import com.google.gson.annotations.SerializedName;

public class Cart_model {
    @SerializedName("cart_id")
    private String cart_id;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("total")
    private String total;
    @SerializedName("price")
    private String price;

    public Cart_model(String cart_id,String product_id, String product_name, int quantity,String total,String price){
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getCart_id() {
        return cart_id;
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

    public String getPrice() {
        return price;
    }

    public String getTotal() {
        return total;
    }
}
