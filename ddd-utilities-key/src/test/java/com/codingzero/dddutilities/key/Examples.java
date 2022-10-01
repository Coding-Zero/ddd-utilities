package com.codingzero.dddutilities.key;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Examples {

    @BeforeEach
    public void setup() {

    }

    @Test
    public void generalTest() {
        String content = "hello world";

        System.out.println("base64 string: " + Key.fromString(content).toBase64String(true));
        System.out.println("base64 hex string: " + Key.fromString(content).toBase64String(true));

        System.out.println("hmac string: " + Key.fromString(content).toHMACKey(HMACKey.Algorithm.SHA256).toString());

        System.out.println("hmac hex #1 string: " + Key.fromString(content).toHMACKey(HMACKey.Algorithm.SHA256).toHexString());
        System.out.println("hmac hex #2 string: " + Key.fromString(content).toHMACKey(HMACKey.Algorithm.SHA256).toHexString());
        System.out.println("random hmac #1 hex string: " + Key.fromString(content).toRandomHMACKey(HMACKey.Algorithm.SHA256).toHexString());
        System.out.println("random hmac #2 hex string: " + Key.fromString(content).toRandomHMACKey(HMACKey.Algorithm.SHA256).toHexString());

        System.out.println("original string: \"" +
                Key.fromBase64String(Key.fromString(content).toBase64String(true), true) + "\"");

        String randomKey = RandomKey.nextTimeBasedUUIDKey().toHMACKey(HMACKey.Algorithm.SHA256).toBase64String(true);
        System.out.println("random time based uuid key: " + randomKey);

        System.out.println("\n\n\n\n\n\n");

        Key randomUUIDKey = RandomKey.nextUUIDKey();
        System.out.println("uuid key in HEX: " + randomUUIDKey.toHexString());
        System.out.println("uuid key in Base64: " + randomUUIDKey.toBase64String(false));
        System.out.println("uuid key in Base64 (url safe): " + randomUUIDKey.toBase64String(true));

        System.out.println("\n");

        Key randomUUIDHMACKey = randomUUIDKey.toHMACKey(HMACKey.Algorithm.SHA256);
        System.out.println("uuid hmac key in HEX: " + randomUUIDHMACKey.toHexString());
        System.out.println("uuid hmac key in Base64: " + randomUUIDHMACKey.toBase64String(false));
        System.out.println("uuid hmac key in Base64 (url safe): " + randomUUIDHMACKey.toBase64String(true));

        System.out.println("\n\n\n\n\n\n");

        Key randomTimeBasedUUIDKey = RandomKey.nextTimeBasedUUIDKey();
        System.out.println("time-based uuid key in HEX: " + randomTimeBasedUUIDKey.toHexString());
        System.out.println("time-based uuid key in Base64: " + randomTimeBasedUUIDKey.toBase64String(false));
        System.out.println("time-based uuid key in Base64 (url safe): " + randomTimeBasedUUIDKey.toBase64String(true));

        System.out.println("\n");

        Key randomTimeBasedUUIDHMACKey = randomTimeBasedUUIDKey.toHMACKey(HMACKey.Algorithm.SHA256);
        System.out.println("time-based uuid hmac (sha-256) key in HEX : " + randomTimeBasedUUIDHMACKey.toHexString());
        System.out.println("time-based uuid hmac (sha-256) key in Base64 : " + randomTimeBasedUUIDHMACKey.toBase64String(false));
        System.out.println("time-based uuid hmac (sha-256) key in Base64 (url safe) : " + randomTimeBasedUUIDHMACKey.toBase64String(true));
    }
}
