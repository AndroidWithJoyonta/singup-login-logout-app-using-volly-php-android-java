package com.scanner.manageaccount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView tvDisplay;
    ImageView imageView;
    Button logutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences =getSharedPreferences("myApp",MODE_PRIVATE);

         tvDisplay = findViewById(R.id.tvDisplay);
         imageView = findViewById(R.id.imageView);
         logutBtn = findViewById(R.id.logutBtn);


        try {
            myMethod.MY_KEY= myMethod.encriptMyData("tapu");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String email = sharedPreferences.getString("email","");

        if (email.length()<=0){
            startActivity(new Intent(MainActivity.this,loginActivity.class));
        }else {
            objectRequest();
        }

        logutBtn.setOnClickListener(v -> {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email","");
            editor.apply();
            startActivity(new Intent(MainActivity.this,loginActivity.class));
            finish();
        });
    }

    //=======================================================
    private void objectRequest(){
        String uri ="https://appsourcecode.xyz/apps/home.php";


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("key",myMethod.MY_KEY);
            jsonObject.put("email",sharedPreferences.getString("email",""));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, uri, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String result = response.getString("result");
                    String image = response.getString("image");

                    tvDisplay.setText(result);

                    Picasso .get().load(image).into(imageView);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
//                progressBar.setVisibility(View.GONE);
//                textView.setText(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//                textView.setText("error");

                new  AlertDialog.Builder(MainActivity.this)
                        .setTitle("server response")
                        .setMessage(error.getMessage())
                        .create().show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);

    }

}