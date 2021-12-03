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
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Random;

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
        Button register_btn2 = findViewById(R.id.register_btn2);
        register_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                Register.this.finish();
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("")
                        ||email.getText().toString().equals("")
                        ||name.getText().toString().equals("")
                        ||age.getText().toString().equals("")
                        ||address.getText().toString().equals("")
                        ||password.getText().toString().equals("")
                        ||c_password.getText().toString().equals("")
                        ||contact_no.getText().toString().equals("")
                       ){
                    Toast.makeText(getBaseContext(), "please fill-up required fields", Toast.LENGTH_SHORT).show();
                }else{

                    if(c_password.getText().toString().equals(password.getText().toString())){
                        api_login_function2(   username.getText().toString()
                            ,email.getText().toString()
                            ,name.getText().toString()
                            ,age.getText().toString(),address.getText().toString()
                            ,password.getText().toString()
                            ,c_password.getText().toString()
                            ,contact_no.getText().toString()
                            ,sex);
                    }else{

                        Toast.makeText(getBaseContext(), "password does not match", Toast.LENGTH_SHORT).show();
                    }

                }



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


    //Function to display the custom dialog.
    void showCustomDialog(final String username,
                          final String email,
                          final String name,
                          final String age,
                          final String address,
                          final String password,
                          final String c_password,
                          final String contact_no,final String sex) {
        Random rand = new Random();
        String id = String.format("%04d", rand.nextInt(10000));
        final Dialog dialog = new Dialog(Register.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.verification_dialog);
        final TextView tv_title = dialog.findViewById(R.id.v_code);

        final Button Cancel_btn = dialog.findViewById(R.id.Cancel_btn);
        final Button Okay_btn = dialog.findViewById(R.id.Okay_btn);

        tv_title.setText("Enter verification code: "+id);
        Okay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                api_login_function2(
                        username
                        ,email
                        ,name
                        ,age,address
                        ,password
                        ,c_password
                        ,contact_no
                        ,sex

                );
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