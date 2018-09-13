package com.example.azri.smartmonitorv2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretKeyGenerator {

    public char[] password = "1234567890".toCharArray();
    private SecretKey key;

    void GenerateKey(KeyStore ks, MainActivity mainActivity){

        //generate key using AES
        try {
            key = KeyGenerator.getInstance("AES").generateKey();

            SaveKey(ks, mainActivity);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    void SaveKey(KeyStore ks, MainActivity mainActivity) {

        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        try {
            ks.setEntry("SecretKeyAlias", secretKeyEntry, null);

            //Save the keystore
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mainActivity.getFilesDir().getAbsolutePath() + "/OEKeyStore");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                ks.store(fos, password);
            } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

    SecretKey LoadKey(KeyStore ks, MainActivity mainActivity) {

        //Load KeyStore
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(mainActivity.getFilesDir().getAbsolutePath() + "/OEKeyStore");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ks.load(fis, password);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }

        //Load the secret key
        KeyStore.SecretKeyEntry secretKeyEntry = null;
        try {
            secretKeyEntry = (KeyStore.SecretKeyEntry)ks.getEntry("SecretKeyAlias", null);
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            e.printStackTrace();
        }
        key = secretKeyEntry.getSecretKey();

        return key;

    }




}
