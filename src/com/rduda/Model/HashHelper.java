package com.rduda.Model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by Robin on 2015-09-28.
 *
 * Handles the hashing of passwords and the generation
 * of the salts used in the hashing.
 *
 * reference: https://crackstation.net/hashing-security.htm#javasourcecode
 */

abstract class HashHelper {
    private final static int ITERATIONS = 65535;
    private final static int KEY_BITS = 1024;
    private final static int SALT_BYTES = 64;
    private final static String ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * Hashes a password with a salt using PBKDF2 with SHA1.
     * @param password plaintext password to be hashed.
     * @param salt for the hashing.
     * @return The resulting hash from the operation.
     */
    public static String hash(String password, String salt) {
        try {
            PBEKeySpec key = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_BITS);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = keyFactory.generateSecret(key).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Hashing Error: NoSuchCipher");
        }
    }

    /**
     * Generates a salt using Secure randoms.
     * @return A generated salt of 512 bits.
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
