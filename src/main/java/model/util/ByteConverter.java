package main.java.model.util;

import java.nio.ByteBuffer;

public class ByteConverter {
    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] boolToBytes(boolean value) {
        return new byte[] {(byte)(value ? 1 : 0)};
    }

    public static byte boolToByte(boolean value) {
        return (byte)(value ? 1 : 0);
    }

    public static boolean byteToBool(byte b) {
        return b == 1;
    }
}
