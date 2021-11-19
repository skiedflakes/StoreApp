package com.example.store.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
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

    int quantity = 1;
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
                showCustomDialog(product_name,product_id);
//                Toast.makeText(context, "Test add to card", Toast.LENGTH_SHORT).show();
//
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

    void add_product(final String user,final String product_id,final String product_name,int qty) {

        String URL = context.getString(R.string.URL)+"add_to_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    String user_id, status,message;
                    JSONObject jo = jsonArray.getJSONObject(0);
                    status = jo.getString("status");
                    message = jo.getString("message");


                    if(status.equals("1")){
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                hashMap.put("user_id", user);
                hashMap.put("product_name", product_name);
                hashMap.put("qty", String.valueOf(qty));
                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }



    //Function to display the custom dialog.
    void showCustomDialog(String product_name,String product_id) {
        quantity=1;
        final Dialog dialog = new Dialog(context);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.quantity_dialog);
//
//        //Initializing the views of the dialog.
        final TextView qty_text = dialog.findViewById(R.id.qty_text);
        final Button minus_btn = dialog.findViewById(R.id.minus_btn);
        final Button plus_btn = dialog.findViewById(R.id.plus_btn);
        final Button Cancel_btn = dialog.findViewById(R.id.Cancel_btn);
        final Button Okay_btn = dialog.findViewById(R.id.Okay_btn);

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity==1){

                }else{
                    quantity--;
                    Log.e("testjr", String.valueOf(quantity));
                    qty_text.setText( String.valueOf(quantity));
                }
            }
        });

        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                quantity++;
                Log.e("testjr", String.valueOf(quantity));
                qty_text.setText( String.valueOf(quantity));
            }
        });

        Okay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                add_product(user,product_id,product_name,quantity);
            }
        });
        Cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });





        dialog.show();
    }


}
