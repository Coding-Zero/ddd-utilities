package com.codingzero.dddutilities.key;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Key {

    private byte[] key;

    private Key(byte[] key) {
        this.key = key.clone();
    }

    public byte[] getKey() {
        return key.clone();
    }

    public String toHexString() {
        return new Hex(getKey()).getText();
    }

    public String toBase64String(boolean isUrlSafe) {
        if (isUrlSafe) {
            return new String(Base64.getUrlEncoder().encode(getKey()));
        }
        return new String(Base64.getEncoder().encode(getKey()));
    }

    @Override
    public String toString() {
        return new String(getKey());
    }

    public Key toHMACKey(HMACKey.Algorithm algorithm) {
        byte[] bytes = new HMACKey(getKey(), getKey(), algorithm).getKey();
        return fromBytes(bytes);
    }

    public Key toRandomHMACKey(HMACKey.Algorithm algorithm) {
        byte[] bytes = new HMACKey(getKey(), algorithm).getKey();
        return fromBytes(bytes);
    }

    public static Key fromBase64String(String str, boolean isUrlSafe) {
        byte[] bytes;
        if (isUrlSafe) {
            bytes = Base64.getUrlDecoder().decode(str);
        } else {
            bytes = Base64.getDecoder().decode(str);
        }
        return new Key(bytes);
    }

    public static Key fromHexString(String str) {
        byte[] bytes = new Hex(str).getBytes();
        return new Key(bytes);
    }

    public static Key fromString(String str) {
        try {
            return new Key(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Key fromBytes(byte[] bytes) {
        return new Key(bytes);
    }

}
