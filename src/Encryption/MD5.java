package Encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe utilitaire fournissant une méthode de hachage MD5.
 * <p>
 * Cette classe permet de générer une empreinte MD5 d'une chaîne de caractères.
 * L'algorithme MD5 produit une empreinte de 128 bits (32 caractères hexadécimaux).
 * </p>
 *
 * <strong>⚠️ Attention :</strong> MD5 n'est pas recommandé pour des usages de sécurité critiques (comme les mots de passe)
 * en raison de ses faiblesses connues (collisions). Utiliser SHA-256 ou bcrypt dans les contextes sensibles.
 *
 * @author
 */

public class MD5 {

    /**
     * Génère l'empreinte MD5 (hash) d'une chaîne de caractères en entrée.
     *
     * @param input La chaîne de caractères à hacher.
     * @return Une chaîne contenant l'empreinte MD5 hexadécimale en minuscules.
     * @throws RuntimeException si l'algorithme MD5 n'est pas disponible (ce qui est peu probable).
     */

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
