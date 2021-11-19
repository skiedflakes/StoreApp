package com.example.store.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    JSONArray jsonArray;
    JSONObject jsonObject;
    View view;

    SessionManager session;
    String user;

    //recycler view
    public RecyclerView recyclerView;
    private Cart_adapter adapter;
    private Cart_adapter2 adapter2;
    ArrayList<Cart_model> file_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    Button btn_submit,btn_cancel;
    TextView status,total_cart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_cart);



        btn_submit = view.findViewById(R.id.btn_submit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        status= view.findViewById(R.id.status);
        total_cart = view.findViewById(R.id.total_cart);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you want to cancel your order?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cancel_order();
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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View  v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you want to submit order?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                submit_cart();
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
        status.setVisibility(View.GONE);

        btn_submit.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        check_cart();

        return view;
    }

    void set_total_cart(String total){
        total_cart.setText(   "TOTAL : "+total);
    }
    void get_cart() {

        btn_submit.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.GONE);
        String URL = getString(R.string.URL)+"get_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("test",response);
                    status.setText("ORDER PENDING");
                    file_list = new ArrayList<Cart_model>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);
                        if(i==0){
                            total_cart.setText(   "TOTAL : "+jsonObject1.getString("total_price"));
                        }
                        file_list.add(new Cart_model(
                                jsonObject1.getString("cart_id"), jsonObject1.getString("product_id"),jsonObject1.getString("product_name"), jsonObject1.getInt("quantity"),jsonObject1.getString("total"),jsonObject1.getString("price")));
                    }

                    adapter = new Cart_adapter(view.getContext(), file_list,DashboardFragment.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
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
    void get_cart_submitted() {
        Log.i("getcart","subbmited");
        String URL = getString(R.string.URL)+"get_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    status.setText("ORDER SUBMITTED");
                    btn_submit.setVisibility(View.GONE);
                    btn_cancel.setVisibility(View.VISIBLE);
                    //((Main)getActivity()).alert_debug(response);
                    file_list = new ArrayList<Cart_model>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);
                        if(i==0){
                            total_cart.setText(  "TOTAL : "+ jsonObject1.getString("total_price"));
                        }
                        file_list.add(new Cart_model(
                                jsonObject1.getString("cart_id"), jsonObject1.getString("product_id"),jsonObject1.getString("product_name"),jsonObject1.getInt("quantity"),jsonObject1.getString("total"),jsonObject1.getString("price")));
                    }

                    adapter2 = new Cart_adapter2(view.getContext(), file_list);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter2);
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
                hashMap.put("user_id", user);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }
    void get_cart_pickup() {
        Log.i("getcart","subbmited");
        String URL = getString(R.string.URL)+"get_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    status.setText("READY FOR PICKUP");
                    btn_submit.setVisibility(View.GONE);
                    btn_cancel.setVisibility(View.GONE);
                    //((Main)getActivity()).alert_debug(response);
                    file_list = new ArrayList<Cart_model>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);
                        if(i==0){
                            total_cart.setText( "TOTAL : "+ jsonObject1.getString("total_price"));
                        }
                        file_list.add(new Cart_model(
                                jsonObject1.getString("cart_id"), jsonObject1.getString("product_id"),jsonObject1.getString("product_name"),jsonObject1.getInt("quantity"),jsonObject1.getString("total"),jsonObject1.getString("price")));
                    }

                    adapter2 = new Cart_adapter2(view.getContext(), file_list);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter2);
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
                hashMap.put("user_id", user);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }

    void check_cart() {

        session = new SessionManager(getContext().getApplicationContext());
        HashMap<String, String> user_account = session.getUserDetails();

        user = user_account.get(SessionManager.KEY_USERID);

        String URL = getString(R.string.URL)+"check_cart_status.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    status.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1 = (JSONObject)jsonArray.get(0);

                    String mstatus = jsonObject1.getString("status");

                    if(mstatus.equals("pending")) {

                        btn_cancel.setVisibility(View.GONE);
                        status.setText("ORDER PENDING");
                        get_cart();

                    }else if(mstatus.equals("requested")) {
                        Toast.makeText(getContext(), "requested", Toast.LENGTH_SHORT).show();
                        status.setText("ORDER SUBMITTED");
                        get_cart_submitted();


                    }else if(mstatus.equals("pickup")) {
                        Toast.makeText(getContext(), "order ready for pickup", Toast.LENGTH_SHORT).show();

                        get_cart_pickup();


                    }else{

                        btn_cancel.setVisibility(View.GONE);
                        status.setText("ORDER PENDING");
                        get_cart();
                    }

                }

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
                hashMap.put("user_id", user);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }
    void submit_cart() {
        String URL = getString(R.string.URL)+"submit_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    String user_id, status;
                    JSONObject jo = jsonArray.getJSONObject(0);
                    status = jo.getString("status");


                    Log.e("hahaha",status);
                    if(status.equals("1")){
                        Toast.makeText(view.getContext(), " Success", Toast.LENGTH_SHORT).show();
                        get_cart_submitted();

                    }else{
                        Toast.makeText(view.getContext(),  "Account not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e){
                    Toast.makeText(view.getContext(),  "Error connection", Toast.LENGTH_SHORT).show();
                }
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
                hashMap.put("user_id", user);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }
    void cancel_order() {
        String URL = getString(R.string.URL)+"cancel_order.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    String user_id, status;
                    JSONObject jo = jsonArray.getJSONObject(0);
                    status = jo.getString("status");


                    Log.e("hahaha",status);
                    if(status.equals("1")){
                        Toast.makeText(view.getContext(), " Success", Toast.LENGTH_SHORT).show();

                        get_cart();
                    }else{
                        Toast.makeText(view.getContext(),  "Account not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e){
                    Toast.makeText(view.getContext(),  "Error connection", Toast.LENGTH_SHORT).show();
                }
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
                hashMap.put("user_id", user);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }

}