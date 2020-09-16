package main.java.model;

import main.java.model.Entity;
import main.java.model.util.Vector2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Bomb extends Entity {

    private int damage = 1;
    private int speed = 5;

    public Bomb(Vector2D position) {
        super(position);
    }

    public void bombUpdate() {
        position.x += speed;
    }

    public int getDamage() {
        return damage;
    }

    public EntityType getType() {
        return EntityType.Bomb;
    }


    public void writeToStream(FileOutputStream stream) throws IOException {
        position.writeToStream(stream);
    }

    public static Bomb readFromStream(FileInputStream stream) throws IOException {
        return new Bomb(Vector2D.readFromStream(stream));
    }
}
