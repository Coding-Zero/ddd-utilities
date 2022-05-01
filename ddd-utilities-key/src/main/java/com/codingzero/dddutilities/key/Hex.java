package com.codingzero.dddutilities.key;

import java.util.Arrays;

public class Hex {

    private byte[] bytes;
    private String text;

    public Hex(String text) {
        this.text = text;
        this.bytes = convertHexTextToBytes(text);

    }

    public Hex(byte[] bytes) {
        this.text = convertBytesToHexText(bytes);
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    public byte[] getBytes() {
        return bytes.clone();
    }

    public String getText() {
        return text;
    }

    private static byte[] convertHexTextToBytes(String text) {
        if (null == text) {
            throw new IllegalArgumentException("Hex text cannot be null value.");
        }
        if (text.length() % 2 != 0) {
            throw new IllegalArgumentException("The length of hex text need to be divided by 2.");
        }
        int len = text.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(text.charAt(i), 16) << 4)
                    + Character.digit(text.charAt(i+1), 16));
        }
        return data;
    }

    private static String convertBytesToHexText(byte[] bytes) {
        if (null == bytes) {
            throw new IllegalArgumentException("Hex bytes cannot be null value.");
        }
        if (bytes.length % 2 != 0) {
            throw new IllegalArgumentException("The length of hex bytes need to be divided by 2.");
        }
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }
}
