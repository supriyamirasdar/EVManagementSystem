package in.nsoft.evmanagementsystem;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.*;

public class EncryptDecrypt {

    /*static Log log = LogFactory.getLog(EncryptDecrypt.class);*/

    private static byte[] key = {0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79};

    public static String encrypt(String strToEncrypt) {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec SecretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, SecretKey);
            byte[] b= Base64.encode(cipher.doFinal(strToEncrypt.getBytes()), Base64.DEFAULT);
           // final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return b.toString();

        } catch (Exception e) {
           
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try 
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec SecretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, SecretKey);
            String decryptedString = new String(cipher.doFinal(Base64.decode(strToDecrypt,Base64.DEFAULT)));
           // byte[] b= Base64.decode(strToDecrypt.getBytes(), Base64.DEFAULT);
            return decryptedString;
        } 
        catch (Exception e) {
           
        	e.printStackTrace();
        }
        return null;
    }
}
