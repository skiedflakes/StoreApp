package com.example.store.ui.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.store.R;
import com.example.store.SessionManager;
import com.example.store.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    Spinner spinner_category;
    ArrayAdapter<String> categoryadapter;
    List<String> category_list, category_ids;
    JSONArray jsonArray;
    JSONObject jsonObject;
    View view;



    //recycler view
    public RecyclerView recyclerView;
    private Product_adapter adapter;
    ArrayList<Product_model> file_list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);




        recyclerView = view.findViewById(R.id.recyclerview_products);
        spinner_category = view.findViewById(R.id.spinner_salesman);
        category_list = new ArrayList<>();
        category_ids = new ArrayList<>();

        categoryadapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, category_list);
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(categoryadapter);


        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        get_categories();
        return view;
    }

    public void get_categories(){

        String URL =getString(R.string.URL)+"get_categories.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("ha",response);
                    jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("data");
                    String category_name, category_id;

                    category_list.add("Select Category");
                    category_ids.add("-1");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jo = jsonArray.getJSONObject(i);
                        category_name = jo.getString("category_name");
                        category_id = jo.getString("category_id");

                        category_ids.add(category_id);
                        category_list.add(category_name);

                    }

                    categoryadapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, category_list);
                    categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_category.setAdapter(categoryadapter);


                    spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = parent.getItemAtPosition(position).toString();

                            String category_id = category_ids.get(position);

//                            get_products(category_id);
                            if(category_id.equals("-1")){

                            }else{
                                get_products(category_id);
                            }
                        }
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(getActivity(), "Failed to load something went wrong", Toast.LENGTH_SHORT).show();
                }catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("employee_id","" );
                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }

    void get_products(String category_id) {
        String URL = getString(R.string.URL)+"get_products.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //((Main)getActivity()).alert_debug(response);
                    file_list = new ArrayList<Product_model>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        file_list.add(new Product_model(
                                jsonObject1.getString("product_id"),jsonObject1.getString("product_name"),1));
                    }

                    adapter = new Product_adapter(view.getContext(), file_list);
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
                hashMap.put("category_id", category_id);

                return hashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        AppController.getInstance().setVolleyDuration(stringRequest);
    }




}