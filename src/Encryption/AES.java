package Encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {

    // Méthode pour générer une clé AES à partir d'une chaîne
    public static SecretKey generateKey(String keyStr) throws Exception {
        byte[] keyBytes = keyStr.getBytes("UTF-8");
        // AES utilise des clés de 16, 24 ou 32 bytes
        byte[] key = new byte[16];
        System.arraycopy(keyBytes, 0, key, 0, Math.min(keyBytes.length, key.length));
        return new SecretKeySpec(key, "AES");
    }

    // Crypter un texte
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Décrypter un texte
    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, "UTF-8");
    }

    /*
    public static void main(String[] args) {
        try {
            String originalString = "Azerty";
            String secret = "ECE"; // Doit être de taille correcte (ou tronqué/paddé)

            SecretKey key = generateKey(secret);

            String encryptedString = encrypt(originalString, key);
            System.out.println("Texte crypté : " + encryptedString);

            String decryptedString = decrypt(encryptedString, key);
            System.out.println("Texte décrypté : " + decryptedString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */
}

