package com.example.store.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.store.AppController;
import com.example.store.R;
import com.example.store.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.UsersViewHolder> {
    private ArrayList<Cart_model> dataList;
    private Context context;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SessionManager session;
    String user;
    private Fragment fragment;
    public Cart_adapter(Context context, ArrayList<Cart_model> dataList, Fragment fragment){
        this.dataList = dataList;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_row, parent, false);

        session = new SessionManager(context.getApplicationContext());
        HashMap<String, String> user_account = session.getUserDetails();

        user = user_account.get(SessionManager.KEY_USERID);



        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder,final int position) {
        int pos = position;
        final String product_name = dataList.get(position).getProduct_name();
        final String cart_id = dataList.get(position).getCart_id();
        final String price = dataList.get(position).getPrice();
        final String total = dataList.get(position).getTotal();
        final int qty = dataList.get(position).getQuantity();

        holder.btn_users.setText(product_name+" x "+String.valueOf(qty));
        holder.btn_price.setText(price);
        holder.btn_total.setText(total);
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to cancel this item?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cancel_cart_item(cart_id, pos);

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        Button btn_users,btn_cancel,btn_total,btn_price;
        UsersViewHolder(View itemView) {
            super(itemView);
            btn_users = itemView.findViewById(R.id.btn_users);

            btn_cancel= itemView.findViewById(R.id.btn_delete);
            btn_total = itemView.findViewById(R.id.btn_total);
            btn_price= itemView.findViewById(R.id.btn_price);
        }
    }


    void cancel_cart_item(final String cart_id,int position) {
        Toast.makeText(context, cart_id, Toast.LENGTH_SHORT).show();
        String URL = context.getString(R.string.URL)+"cancel_cart_item.php";
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
                        dataList.remove(position);
                        notifyDataSetChanged();
                        set_total();
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
                hashMap.put("cart_id", cart_id);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }

    void set_total() {
        String URL = context.getString(R.string.URL)+"get_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("test",response);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        ((DashboardFragment) fragment).set_total_cart("0");
                    }else{
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);
                            if(i==0){
                                ((DashboardFragment) fragment).set_total_cart(jsonObject1.getString("total_price"));
                            }
                        }
                    }

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("user_id",user);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }



}
