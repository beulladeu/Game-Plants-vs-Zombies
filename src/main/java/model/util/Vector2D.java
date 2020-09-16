package main.java.model.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Vector2D {

    public int x;
    public int y;

    public Vector2D() {}

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int SqrDistance() {
        return x * x + y * y;
    }

    public int Distance() {
        return (int) Math.sqrt(SqrDistance());
    }

    public Vector2D Add(Vector2D value) {
        return new Vector2D(this.x + value.x, this.y + value.y);
    }

    public Vector2D Add(int x, int y) {
        return new Vector2D(this.x + x, this.y + y);
    }

    public Vector2D Sub(Vector2D value) {
        return new Vector2D(this.x - value.x, this.y - value.y);
    }

    public Vector2D Sub(int x, int y) {
        return new Vector2D(this.x - x, this.y - y);
    }

    public Vector2D Mul(int value) {
        return new Vector2D(this.x * value, this.y * value);
    }

    public Vector2D Div(int value) {
        return new Vector2D(this.x / value, this.y / value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Vector2D inp = (Vector2D) o;
        if(this.x == inp.x && this.y == inp.y) return true;
        return false;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }


    public void writeToStream(FileOutputStream stream) throws IOException {
        stream.write(ByteConverter.intToBytes(x));
        stream.write(ByteConverter.intToBytes(y));
    }


    public static Vector2D readFromStream(FileInputStream stream) throws IOException {
        byte[] bytes = new byte[4];
        stream.read(bytes);
        int x = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        int y = ByteConverter.bytesToInt(bytes);
        return new Vector2D(x, y);
    }
}
