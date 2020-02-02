package edu.temple.asymmetricencryption;

import android.util.Base64;

import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class KeyServiceTest {

    // 1) Request a keypair
    @Test
    public void requestKeypair() {
        KeyService service = new KeyService();
        KeyPair kpTest = service.getMyKeyPair();

        assertNotNull(kpTest);
    }
    // 2) Arbitrary text can be encrypted and decrypted using the returned keypair
    @Test
    public void encryptDecrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyService service = new KeyService();
        KeyPair keyPair = service.getMyKeyPair();
        String text = "This is a test";

        // Encrypt
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        byte[] encrypted = cipher.doFinal(text.getBytes());

        //Decrypt
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
        String decrypted = new String(cipher.doFinal(encrypted));

        assertEquals(text, decrypted);
    }

    // 3) A stored keypair can be retrieved
    @Test
    public void requestStoredKeypair() {
        KeyService service = new KeyService();

        // Generate keyPair
        KeyPair keyPair1 = service.getMyKeyPair();
        // Retrieve keyPair
        KeyPair keyPair2 = service.getMyKeyPair();

        // Convert both to string for testability
        PrivateKey privateKey1 = keyPair1.getPrivate();
        PrivateKey privateKey2 = keyPair2.getPrivate();
        String s1 = privateKey1.toString();
        String s2 = privateKey2.toString();

        // Test the key string was the same and not regenerated
        assertEquals(s1, s2);
    }

    // 4) A stored key can be retrieved using partnerName
    @Test
    public void requestStoredPublicKey() throws InvalidKeySpecException {
        // Generate "sender"'s public key and convert to string
        KeyService service1 = new KeyService();
        KeyPair senderKeyPair = service1.getMyKeyPair();
        PublicKey senderPublicKey = senderKeyPair.getPublic();
        byte[] publicKeyBytes = Base64.encode(senderPublicKey.getEncoded(),0);
        String senderKey = new String(publicKeyBytes);

        // Store the key
        KeyService service2 = new KeyService();
        service2.storePublicKey("sender", senderKey);

        // Retrieve the key
        RSAPublicKey retrievedPublicKey = service2.getPublicKey("sender");
        byte[] publicKeyBytes2 = Base64.encode(retrievedPublicKey.getEncoded(),0);
        String retrievedKey = new String(publicKeyBytes2);


        assertEquals(senderKey, retrievedKey);
    }
}
