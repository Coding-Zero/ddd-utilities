package com.codingzero.dddutilities.key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HMACKey {

    private byte[] secretKey;
    private byte[] data;
    private Algorithm algorithm;
    private byte[] key;

    public HMACKey(byte[] data, Algorithm algorithm) {
        this(generateRandomSecretKey(), data, algorithm);
    }

    public HMACKey(byte[] secretKey, byte[] data, Algorithm algorithm) {
        this.secretKey = secretKey.clone();
        this.data = data.clone();
        checkForNullData();
        this.algorithm = algorithm;
        this.key = generateKey(secretKey, data, algorithm.getName());
    }

    public byte[] getSecretKey() {
        return secretKey.clone();
    }

    public byte[] getData() {
        return data.clone();
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public byte[] getKey() {
        return key.clone();
    }

    private void checkForNullData() {
        if (getData() == null) {
            throw new IllegalArgumentException("Data cannot be null value.");
        }
    }

    private static byte[] generateRandomSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[24];
        random.nextBytes(key);
        return key;
    }

    private static byte[] generateKey(byte[] secretKey, byte[] data, String algorithm) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);
            return mac.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Algorithm {
        SHA1("HmacSHA1"),
        SHA256("HmacSHA256"),
        SHA512("HmacSHA512")
        ;

        private final String name;

        Algorithm(String name) {
            this.name = name;
        }

        public String toString() {
            return this.getName();
        }

        public String getName() {
            return name;
        }
    }

}
