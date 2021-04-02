package bsep.admin.utils;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

@Component
public class CryptingUtil {

    // data = user email + secret word
    public byte[] encrypt(String data, PublicKey key) {
        try {
            // Kada se definise sifra potrebno je navesti njenu konfiguraciju, sto u ovom slucaju ukljucuje:
            //	- Algoritam koji se koristi (ECIES)
            //	- Rezim rada tog algoritma (ECB)
            //	- Strategija za popunjavanje poslednjeg bloka (PKCS1Padding)
            //	- Provajdera kriptografskih funckionalnosti (BC)
            Cipher rsaCipherEnc = Cipher.getInstance("ECIES", "BC");

            // inicijalizacija za sifrovanje
            rsaCipherEnc.init(Cipher.ENCRYPT_MODE, key);

            // sifrovanje
            return rsaCipherEnc.doFinal(data.getBytes());
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
                | IllegalStateException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] cipherText, PrivateKey key) {
        try {
            Cipher rsaCipherDec = Cipher.getInstance("ECIES", "BC");

            // inicijalizacija za desifrovanje
            rsaCipherDec.init(Cipher.DECRYPT_MODE, key);

            // desifrovanje
            return rsaCipherDec.doFinal(cipherText);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
                | IllegalStateException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
