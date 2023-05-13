package com.example.contacts;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProfileGenerator {
    public String generateProfilePicUrl(int size) throws NoSuchAlgorithmException {
        // Generate a random string
        String randomString = generateRandomString();

        // Compute a hash of the random string
        byte[] hash = computeHash(randomString.getBytes());

        // Convert the hash to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        // Construct a URL for a random image with the specified size and the hexadecimal hash
        String url = "https://www.gravatar.com/avatar/" + hexString.toString() + "?s=" + size;

        return url;
    }

    private static String generateRandomString() {
        // Generate a random string of length 16
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            char c = (char) (random.nextInt(26) + 'a');
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private static byte[] computeHash(byte[] input) throws NoSuchAlgorithmException {
        // Compute a SHA-256 hash of the input bytes
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input);
        return hash;
    }
}

