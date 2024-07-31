package com.scanner.manageaccount;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    TextInputEditText inputEmail,inputPass;
Button btnSingUp,btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSingUp=findViewById(R.id.btnSingUp);
        inputEmail=findViewById(R.id.inputEmail);
        inputPass=findViewById(R.id.inputPass);
        btnLogin=findViewById(R.id.btnLogin);

        btnSingUp.setOnClickListener(v -> {

            startActivity(new Intent(loginActivity.this,singUpActivity.class));
        });

        btnLogin.setOnClickListener(v -> {





            String uri = "https://appsourcecode.xyz/apps/login.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //progressBar.setVisibility(View.GONE);
//                    textView.setText(response);



                    if (response.contains("VALID LOGIN")){

                        SharedPreferences sharedPreferences = getSharedPreferences("myApp",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", inputEmail.getText().toString());
                        editor.apply();


                        startActivity(new Intent(loginActivity.this, MainActivity.class));
                    }else {
                        new  AlertDialog.Builder(loginActivity.this)
                                .setTitle("server response")
                                .setMessage(response)
                                .create().show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    textView.setText("Error");

                    //progressBar.setVisibility(View.GONE);

                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map myMap = new HashMap<String,String>();


                    try {
                        myMap.put("email",myMethod.encriptMyData(inputEmail.getText().toString()));
                        myMap.put("password",myMethod.encriptMyData(inputPass.getText().toString()));
                        myMap.put("key",myMethod.MY_KEY);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }



                    return myMap;
                }
            };


            RequestQueue requestQueue= Volley.newRequestQueue(loginActivity.this);
            requestQueue.add(stringRequest);



        });



    }
    //-------------------------
}