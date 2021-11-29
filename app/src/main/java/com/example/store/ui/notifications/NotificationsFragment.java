package com.example.store.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.store.AppController;
import com.example.store.R;
import com.example.store.SessionManager;

import com.example.store.ui.home.Product_model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    JSONArray jsonArray;
    JSONObject jsonObject;
    View view;

    SessionManager session;
    String user;

    //recycler view
    public RecyclerView recyclerView;
    private History_adapter adapter;
    ArrayList<History_model> file_list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_history);

        session = new SessionManager(getContext().getApplicationContext());
        HashMap<String, String> user_account = session.getUserDetails();

        user = user_account.get(SessionManager.KEY_USERID);


        get_products();
        return view;
    }

    void get_products() {
        String URL = getString(R.string.URL)+"get_finished_transactions.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    //((Main)getActivity()).alert_debug(response);
                    file_list = new ArrayList<History_model>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        file_list.add(new History_model(
                                jsonObject1.getString("details")));
                    }

                    adapter = new History_adapter(view.getContext(), file_list);
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
                hashMap.put("user_id", user);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }



}