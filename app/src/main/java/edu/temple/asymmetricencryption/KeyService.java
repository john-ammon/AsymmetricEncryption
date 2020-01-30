package edu.temple.asymmetricencryption;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;

public class KeyService extends Service {
    public KeyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //Generate and/or retrieve a user’s RSA KeyPair. The first call to this method will generate
    // and store the keypair before returning it. Subsequent calls will return the same key pair
    KeyPair getMyKeyPair() {

        KeyPair keyPair;
        KeyPairGenerator kpg;

        // TODO: if the keypair does not exist yet
        if(true) {
            try {
                kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048, new SecureRandom());
                keyPair = kpg.generateKeyPair();
                return keyPair;
            } catch(NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: get the stored keypair
        }
        return null;
    }

    //Store a key for a provided partner name
    void storePublicKey(String partnerName) {

    }

    //Returns the public key associated with the provided partner name
    RSAPublicKey getPublicKey(String partnerName) {
        return new RSAPublicKey() {
            @Override
            public BigInteger getPublicExponent() {
                return null;
            }

            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }

            @Override
            public BigInteger getModulus() {
                return null;
            }
        };
    }
}
