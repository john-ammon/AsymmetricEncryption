package edu.temple.asymmetricencryption;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class KeyService extends Service {

    Map<String, String> storedPublicKeys;
    PublicKey myPublicKey;
    PrivateKey myPrivateKey;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        KeyService getService() {
            return KeyService.this;
        }
    }

    public KeyService() {
        storedPublicKeys = new HashMap<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        
        return mBinder;
    }

    //Generate and/or retrieve a user’s RSA KeyPair. The first call to this method will generate
    // and store the keypair before returning it. Subsequent calls will return the same key pair
    KeyPair getMyKeyPair() {

        KeyPair keyPair;

        // if the keypair does not exist yet
        if(myPrivateKey == null || myPrivateKey == null) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
                myPrivateKey = keyPair.getPrivate();
                myPublicKey = keyPair.getPublic();
                return keyPair;
            } catch(NoSuchAlgorithmException e) { e.printStackTrace(); }
        // else the keypair does exist so retrieve it
        } else {
            keyPair = new KeyPair(myPublicKey, myPrivateKey);
            return keyPair;
        }
        return null;
    }

    //Store a key for a provided partner name
    void storePublicKey(String partnerName, String publicKey) {
        storedPublicKeys.put(partnerName, publicKey);
    }

    //Returns the public key associated with the provided partner name
    RSAPublicKey getPublicKey(String partnerName) throws InvalidKeySpecException {
        String publicKey = storedPublicKeys.get(partnerName);
        byte[] publicBytes = Base64.decode(publicKey, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = null;
        
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return rsaPublicKey;
    }
}
