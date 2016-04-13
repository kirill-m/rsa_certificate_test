package com.example.kirill.rsa;

import android.content.Intent;
import android.os.Bundle;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        KeyChainAliasCallback {

    private static final String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RSAEncryption rsaInstance = new RSAEncryption();
        String message = "Hello";


        try {
            rsaInstance.generateKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] enc = new byte[0];
        try {
            enc = rsaInstance.encrypt(message);
            String decr = rsaInstance.decrypt(enc);

        } catch (Exception e) {
            e.printStackTrace();
        }

        CertificateFactory certFactory = null;
        try {
            certFactory = CertificateFactory.getInstance("X.509");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = KeyChain.createInstallIntent();
        byte[] bytes = rsaInstance.getPublicKey().getEncoded();
        InputStream in = new ByteArrayInputStream(bytes);

        X509Certificate cert = null;
        try {
            cert = (X509Certificate)certFactory.generateCertificate(in);
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        /* В этом случае НЕ выводится окошко */
        intent.putExtra(KeyChain.EXTRA_PKCS12,cert);

        /* В этом случае выводится окошко */
        //intent.putExtra(KeyChain.EXTRA_PKCS12,cert);

        startActivity(intent);

    }

    @Override
    public void alias(final String alias) {
        Log.d(TAG, "Thread: " + Thread.currentThread().getName());
        Log.d(TAG, "selected alias: " + alias);
    }

    @Override
    public void onClick(View v) {
        KeyChain.choosePrivateKeyAlias(this, this,
                new String[]{"RSA"}, null, null, -1, null);
    }
}
