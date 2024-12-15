package sample.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
    * The class responsible for encrypting data using various encryption algorithms.
 * This class provides methods to encrypt passwords and other sensitive data
 * using encryption types such as Base64, MD5, Feistel cipher, salted encryption,
 * and no encryption.
 */
public class Encryption {
    private static final Logger logger = LogManager.getLogger(Encryption.class);

    // Number of rounds for the Feistel cipher
    private static final int ROUNDS = 16;

    // Key for the Feistel cipher
    private static final String KEY = "I_wonna_be_the_boshi";

    /**
     * Encrypts the given password using the specified encryption type.
     *
     * @param password        The password to encrypt.
     * @param encryptionType  The type of encryption to use (e.g., "Base64", "md5", "Feistel cipher", "Salt", "Without cipher").
     * @return The encrypted password.
     * @throws NoSuchAlgorithmException If the specified encryption algorithm is not available.
     */
    public static String encrypt(String password, String encryptionType) throws NoSuchAlgorithmException {
        logger.debug("Encrypting password with type: {}", encryptionType);
        switch (encryptionType) {
            case "Base64":
                return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
            case "md5":
                return md5(password);
            case "Feistel cipher":
                return feistelEncrypt(password);
            case "Salt":
                return saltedEncrypt(password);
            case "Without cipher":
                return password;
            default:
                throw new IllegalArgumentException("Unsupported encryption type");
        }
    }

    /**
     * Encrypts the given password using the MD5 algorithm.
     *
     * @param password The password to encrypt.
     * @return The MD5 hash of the password.
     */
    private static String md5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            logger.debug("MD5 hash generated: {}", hexString);
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException occurred", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypts the given plain text using the Feistel cipher.
     *
     * @param plainText The plain text to encrypt.
     * @return The encrypted text in Base64 format.
     */
    private static String feistelEncrypt(String plainText) {
        logger.debug("Encrypting with Feistel cipher");
        byte[] data = plainText.getBytes(StandardCharsets.UTF_8);
        int length = data.length;

        // If the length is odd, add a null byte at the end
        if (length % 2 != 0) {
            byte[] newData = new byte[length + 1];
            System.arraycopy(data, 0, newData, 0, length);
            data = newData;
            length++;
        }

        // Split the data into two equal parts
        byte[] left = new byte[length / 2];
        byte[] right = new byte[length / 2];
        System.arraycopy(data, 0, left, 0, length / 2);
        System.arraycopy(data, length / 2, right, 0, length / 2);

        // Feistel cipher rounds
        for (int i = 0; i < ROUNDS; i++) {
            byte[] newRight = new byte[right.length];
            for (int j = 0; j < right.length; j++) {
                newRight[j] = (byte) (left[j] ^ feistelFunction(right[j], KEY.getBytes(StandardCharsets.UTF_8)[i % KEY.length()]));
            }
            left = right;
            right = newRight;
        }

        // Combine the left and right parts
        byte[] encryptedData = new byte[length];
        System.arraycopy(left, 0, encryptedData, 0, left.length);
        System.arraycopy(right, 0, encryptedData, left.length, right.length);

        // Return the encrypted data in Base64 format
        String encryptedText = Base64.getEncoder().encodeToString(encryptedData);
        logger.debug("Feistel cipher encrypted text: {}", encryptedText);
        return encryptedText;
    }

    /**
     * Simple Feistel function.
     *
     * @param input The input byte.
     * @param key   The key byte.
     * @return The result of the Feistel function.
     */
    private static byte feistelFunction(byte input, byte key) {
        return (byte) (input ^ key);
    }

    /**
     * Encrypts the given password using salted encryption.
     *
     * @param password The password to encrypt.
     * @return The salted and hashed password in Base64 format.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    private static String saltedEncrypt(String password) throws NoSuchAlgorithmException {
        logger.debug("Encrypting with salted encryption");
        // Salt value
        byte[] SALT = "AnotherOneBitesTheDust".getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(SALT);
        byte[] hashedPassword = md.digest(password.getBytes());
        String encryptedText = Base64.getEncoder().encodeToString(hashedPassword);
        logger.debug("Salted encrypted text: {}", encryptedText);
        return encryptedText;
    }
}