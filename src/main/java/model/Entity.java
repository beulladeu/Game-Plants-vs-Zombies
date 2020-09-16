package main.java.model;

import main.java.model.util.Vector2D;

public abstract class Entity {

    protected Vector2D position;

    protected Entity(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    abstract EntityType getType();


    public enum EntityType {
        Sun,
        SunFlower,
        GunFlower,
        Shovel,
        Zombie,
        Bomb,
    };
}
