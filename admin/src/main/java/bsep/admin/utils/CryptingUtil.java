package bsep.admin.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

@Component
public class CryptingUtil {

    public CryptingUtil() {
        Security.addProvider(new BouncyCastleProvider());
    }
    // data = user email + secret word
    public byte[] encrypt(String data, PublicKey key) {
        try {
            // Kada se definise sifra potrebno je navesti njenu konfiguraciju, sto u ovom slucaju ukljucuje:
            //	- Algoritam koji se koristi (RSA)
            //	- Rezim rada tog algoritma (ECB)
            //	- Strategija za popunjavanje poslednjeg bloka (PKCS1Padding)
            //	- Provajdera kriptografskih funckionalnosti (BC)
            Cipher rsaCipherEnc = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");

            // inicijalizacija za sifrovanje
            rsaCipherEnc.init(Cipher.ENCRYPT_MODE, key);

            // sifrovanje
            byte[] cipherText = rsaCipherEnc.doFinal(data.getBytes());
            return cipherText;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
                | IllegalStateException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] cipherText, PrivateKey key) {
        try {
            Cipher rsaCipherDec = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");

            // inicijalizacija za desifrovanje
            rsaCipherDec.init(Cipher.DECRYPT_MODE, key);

            // desifrovanje
            byte[] plainText = rsaCipherDec.doFinal(cipherText);
            return plainText;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
                | IllegalStateException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
