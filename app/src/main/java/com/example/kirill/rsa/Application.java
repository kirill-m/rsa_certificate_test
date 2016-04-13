package com.example.kirill.rsa;

/**
 * Created by kirill on 13.04.16.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RSAEncryption rsaInstance = new RSAEncryption();
        String message = "Hello";

        try {
            rsaInstance.generateKeys();
            byte[] enc = rsaInstance.encrypt(message);
            String decr = rsaInstance.decrypt(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

