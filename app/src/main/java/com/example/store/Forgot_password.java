package com.example.store;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Forgot_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText c_password = findViewById(R.id.c_password);
        Button register_btn = findViewById(R.id.register_btn);
        Button register_btn2 = findViewById(R.id.register_btn2);
        register_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Forgot_password.this, Login.class);
                startActivity(i);
                Forgot_password.this.finish();
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username.getText().toString().equals("")||email.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Please fill up required fields", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.getText().toString().equals("")||c_password.getText().toString().equals("")){
                        Toast.makeText(getBaseContext(), "password does not match", Toast.LENGTH_SHORT).show();
                    }else{
                        api_login_function2( username.getText().toString()
                                ,email.getText().toString()
                                ,password.getText().toString()
                                ,c_password.getText().toString());
                    }

                }



            }
        });
    }

    void api_login_function2(final String username,
                             final String email,
                             final String password,
                             final String c_password
) {

        if(c_password.equals(password)){    String URL = getString(R.string.URL) + "reset_password.php";
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {


                        if(response.equals("1")){
                            Toast.makeText(Forgot_password.this, "Reset Password Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Forgot_password.this, Login.class);
                            startActivity(i);
                            Forgot_password.this.finish();

                        }else{
                            Toast.makeText(Forgot_password.this, "Account not found", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e){
                        Toast.makeText(Forgot_password.this, "Error connection", Toast.LENGTH_SHORT).show();
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
                    params.put("password", password);
                    return params;
                }
            }; AppController.getInstance().addToRequestQueue(sr);
            AppController.getInstance().setVolleyDuration(sr);
        }else {

            Toast.makeText(getApplicationContext(), "Passwords does not match", Toast.LENGTH_SHORT).show();

        }

    }

}