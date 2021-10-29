package com.example.store.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.store.AppController;
import com.example.store.Login;
import com.example.store.MainActivity;
import com.example.store.R;
import com.example.store.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product_adapter extends RecyclerView.Adapter<Product_adapter.UsersViewHolder> {
    private ArrayList<Product_model> dataList;
    private Context context;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SessionManager session;
    String user;

    public Product_adapter(Context context, ArrayList<Product_model> dataList){
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.users_card_row, parent, false);

        session = new SessionManager(context.getApplicationContext());
        HashMap<String, String> user_account = session.getUserDetails();

        user = user_account.get(SessionManager.KEY_USERID);



        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder,final int position) {

        final String product_name = dataList.get(position).getProduct_name();
        final String product_id = dataList.get(position).getProduct_id();

        holder.btn_users.setText(product_name);


        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Test add to card", Toast.LENGTH_SHORT).show();
                api_login_function(user,product_id,product_name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        Button btn_users,add_to_cart;
        UsersViewHolder(View itemView) {
            super(itemView);
            btn_users = itemView.findViewById(R.id.btn_users);
            add_to_cart = itemView.findViewById(R.id.add_to_cart);
        }
    }

    void api_login_function(final String user,final String product_id,final String product_name) {

        String URL =  "http://192.168.0.27/kstore/api/add_to_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    String user_id, status;
                    JSONObject jo = jsonArray.getJSONObject(0);
                    status = jo.getString("status");


                    if(status.equals("1")){

                    }else{

                    }

                } catch (Exception e){
                    Toast.makeText(context, "Error connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                }catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("product_id", product_id);
                hashMap.put("user_id", "3");
                hashMap.put("product_name", product_name);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }

}
