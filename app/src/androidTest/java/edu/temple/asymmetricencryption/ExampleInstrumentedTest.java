package edu.temple.asymmetricencryption;

import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
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
public class ExampleInstrumentedTest {

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

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        byte[] encrypted = cipher.doFinal(text.getBytes());
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
}
