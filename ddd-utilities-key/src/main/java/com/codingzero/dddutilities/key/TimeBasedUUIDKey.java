package com.codingzero.dddutilities.key;

import java.util.UUID;

public class TimeBasedUUIDKey {

    private UUID uuid;
    private byte[] key; //16 bytes array.

    public TimeBasedUUIDKey(UUID uuid) {
        this.uuid = uuid;
        checkForInvalidUUID();
        this.key = new Hex(generateKey()).getBytes();
    }

    public UUID getUUID() {
        return uuid;
    }

    private void checkForInvalidUUID() {
        String uuidString = getUUID().toString();
        if (Integer.valueOf(uuidString.substring(14, 15)) != 1) {
            throw new IllegalArgumentException("Only version 1 (Time-based) UUID accepted.");
        }
    }

    private String generateKey() {
        String uuidString = getUUID().toString();
        StringBuilder str = new StringBuilder();
        str.append(uuidString.substring(14, 18));
        str.append(uuidString.substring(9, 13));
        str.append(uuidString.substring(0, 8));
        str.append(uuidString.substring(19, 23));
        str.append(uuidString.substring(24));
        return str.toString();
    }

    public byte[] getKey() {
        return key.clone();
    }

}
