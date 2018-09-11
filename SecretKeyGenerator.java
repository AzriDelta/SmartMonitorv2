package com.example.azri.smartmonitorv2;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

public class SecretKeyGenerator {

    String generateKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // AES is currently available in three key sizes: 128, 192 and 256 bits.The design and strength of all key lengths of the AES algorithm are sufficient to protect classified information up to the SECRET level
        return (String.valueOf(keyGenerator.generateKey()));

    }


}
