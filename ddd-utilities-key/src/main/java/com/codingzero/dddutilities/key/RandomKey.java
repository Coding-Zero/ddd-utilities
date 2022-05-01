package com.codingzero.dddutilities.key;

import com.fasterxml.uuid.Generators;

import java.nio.ByteBuffer;
import java.util.UUID;

public class RandomKey {

    public static Key nextUUIDKey() {
        UUID uuid = UUID.randomUUID();
        return Key.fromBytes(convertUUIDToBytes(uuid));
    }

    private static byte[] convertUUIDToBytes(UUID uuid) {
        if (null == uuid) {
            return null;
        }
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static Key nextTimeBasedUUIDKey() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        TimeBasedUUIDKey uuidKey = new TimeBasedUUIDKey(uuid);
        return Key.fromBytes(uuidKey.getKey());
    }

}
