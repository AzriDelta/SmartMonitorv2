package com.example.azri.smartmonitorv2;

import android.util.Log;

import tgio.rncryptor.RNCryptorNative;

public class dataCrypt {

    private static String generatedSecretKey = "";
    private static final String TAG = null;

    private RNCryptorNative rncryptor = new RNCryptorNative();
    private SecretKeyGenerator generateKey = new SecretKeyGenerator();
    private SecretKeyGenerator seckeygen = new SecretKeyGenerator();

    //String rawUsername = "delta", rawPassword = "delta12345";


    //Fetch generatedSecretKey from Main Activity

    void fetchSecretKey(String seckey) {
        Log.d(TAG, "Generated Secret Key: " + seckey);
        generatedSecretKey = seckey;
    }

    //encrypt the data
    String encryptAndPush(String plainText){
        return new String(rncryptor.encrypt(plainText, generatedSecretKey));
    }

    //decrypt the data
    String decryptAndRetrieve(String cipherText){

        return rncryptor.decrypt(cipherText, generatedSecretKey);
    }


}
