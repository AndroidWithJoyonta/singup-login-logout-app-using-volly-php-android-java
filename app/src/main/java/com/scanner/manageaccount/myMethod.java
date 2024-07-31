package com.scanner.manageaccount;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class myMethod {


    public static  String MY_KEY ="";
    public static String  encriptMyData(String Text ) throws Exception{


        String plainText = Text;

        byte[] plainTextByte =plainText.getBytes("UTF-8");

        String password = "0123456789123456"; //16bit pass
        byte[] passwordByte = password.getBytes("UTF-8");

        SecretKeySpec secretKeySpec = new SecretKeySpec(passwordByte,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        byte[] secouredBytes = cipher.doFinal(plainTextByte);


        String encodeString = Base64.encodeToString(secouredBytes,Base64.DEFAULT);
        return encodeString;
    }
}
