package com.example.store;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        Button login = findViewById(R.id.login_btn);
        Button sign_up = findViewById(R.id.sign_up);
        Button forgot_pass = findViewById(R.id.forgot_pass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(Login.this, username.getText().toString()+" "+password.getText().toString(), Toast.LENGTH_SHORT).show();
                api_login_function2(username.getText().toString(),password.getText().toString());
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                Login.this.finish();
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Forgot_password.class);
                startActivity(i);
                Login.this.finish();
            }
        });
    }

    void api_login_function2(final String input_username, final String input_password) {

        String URL = getString(R.string.URL) + "login.php";
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    String user_id, status;
                    JSONObject jo = jsonArray.getJSONObject(0);
                    status = jo.getString("status");
                    user_id = jo.getString("user_id");

                    if(status.equals("1")){
                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();

                        session.createLoginSession(user_id);

                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        Login.this.finish();
                    }else{
                        Toast.makeText(Login.this, "Account not found", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e){
                    Toast.makeText(Login.this, "Error connection", Toast.LENGTH_SHORT).show();
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
                params.put("username", input_username);
                params.put("password", input_password);
                return params;
            }
        }; AppController.getInstance().addToRequestQueue(sr);
        AppController.getInstance().setVolleyDuration(sr);
    }

}