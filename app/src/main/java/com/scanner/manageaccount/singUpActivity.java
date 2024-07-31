package com.scanner.manageaccount;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.security.identity.IdentityCredentialException;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.scanner.manageaccount.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class singUpActivity extends AppCompatActivity {

   ImageView imageProfile;
   TextView tvChangePhoto;
    TextInputEditText inputEmail, inputPassword,inputName;
    Button btnSingUp,btnLogin;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        imageProfile= findViewById(R.id.imageProfile);
        tvChangePhoto= findViewById(R.id.tvChangePhoto);
        inputEmail= findViewById(R.id.inputEmail);
        inputPassword= findViewById(R.id.inputPassword);
        inputName= findViewById(R.id.inputName);
        btnSingUp= findViewById(R.id.btnSingUp);
        btnLogin= findViewById(R.id.btnLogin);
        progressBar= findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(singUpActivity.this,loginActivity.class));
        });


        btnSingUp.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String name = inputName.getText().toString();

            BitmapDrawable  bitmapDrawable = (BitmapDrawable) imageProfile.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            String image = Base64.encodeToString(imageBytes,Base64.DEFAULT);



             String uri = "https://appsourcecode.xyz/apps/singup.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   progressBar.setVisibility(View.GONE);
//                    textView.setText(response);



                   if (response.contains("singup success")){

                       SharedPreferences sharedPreferences = getSharedPreferences("myApp",MODE_PRIVATE);
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putString("email",email);
                       editor.apply();


                       startActivity(new Intent(singUpActivity.this, MainActivity.class));
                   }else {

                   }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    textView.setText("Error");
                   /* new  AlertDialog.Builder(singUpActivity.this)
                            .setTitle("error")
                            .create().show();
                    progressBar.setVisibility(View.GONE);*/

                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map myMap = new HashMap<String,String>();


                    try {
                        myMap.put("email",myMethod.encriptMyData(inputEmail.getText().toString()));
                        myMap.put("password",myMethod.encriptMyData(inputPassword.getText().toString()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    myMap.put("name",name);
                    myMap.put("image",image);
                    myMap.put("key",myMethod.MY_KEY);

                    return myMap;
                }
            };


            RequestQueue requestQueue= Volley.newRequestQueue(singUpActivity.this);
            requestQueue.add(stringRequest);

        });

        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode()== Activity.RESULT_OK){

                            Intent intent = result.getData();

                            Uri uri = intent.getData();

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                imageProfile.setImageBitmap(bitmap);
                            }catch (IOException e){
                                throw new RuntimeException(e);
                            }


                        }
                    }
                });

        tvChangePhoto.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            launcher.launch(intent);
                            return null;
                        }
                    });
        });
    }
}