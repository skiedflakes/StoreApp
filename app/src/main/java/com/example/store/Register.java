package com.example.store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    Spinner spinner_category;
    ArrayAdapter<String> categoryadapter;
    List<String> category_list, category_ids;

    String sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner_category = findViewById(R.id.spinner_salesman);
        category_list = new ArrayList<>();
        category_ids = new ArrayList<>();
        category_list.add("male");
        category_ids.add("1");
        category_list.add("female");
        category_ids.add("2");
        categoryadapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, category_list);
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(categoryadapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
                sex = selectedItem;
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.email);
        EditText name = findViewById(R.id.name);
        EditText age = findViewById(R.id.age);
        EditText address = findViewById(R.id.address);
        EditText password = findViewById(R.id.password);
        EditText c_password = findViewById(R.id.c_password);
        EditText contact_no = findViewById(R.id.contact_no);
        Button register_btn = findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_login_function2(
                        username.getText().toString()
                        ,email.getText().toString()
                        ,name.getText().toString()
                        ,age.getText().toString(),address.getText().toString()
                        ,password.getText().toString()
                        ,c_password.getText().toString()
                        ,contact_no.getText().toString()
                        ,sex

                );
            }
        });
    }

    void api_login_function2(final String username,
                             final String email,
                             final String name,
                             final String age,
                             final String address,
                             final String password,
                             final String c_password,
                             final String contact_no,final String sex) {

        if(c_password.equals(password)){    String URL = getString(R.string.URL) + "add_user.php";
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("testjr",response);

                        if(response.equals("1")){
                            Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Register.this, Login.class);
                            startActivity(i);
                            Register.this.finish();

                        }else{
                            Toast.makeText(Register.this, "Account not found", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e){
                        Toast.makeText(Register.this, "Error connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("name", name);
                    params.put("age", age);
                    params.put("address", address);
                    params.put("password", password);
                    params.put("contact_no", contact_no);
                    params.put("sex", sex);
                    return params;
                }
            }; AppController.getInstance().addToRequestQueue(sr);
            AppController.getInstance().setVolleyDuration(sr);
        }else {

            Toast.makeText(getApplicationContext(), "Passwords does not match", Toast.LENGTH_SHORT).show();

        }

    }


}