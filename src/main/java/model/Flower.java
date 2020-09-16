package main.java.model;

import main.java.model.util.Vector2D;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Flower extends Entity {
    protected int plantCost;
    protected boolean isEated = false;
    protected int hp;

    public Flower(Vector2D position){
        super(position);
    }

    public int getHp(){
        return this.hp;
    }
    public void consumeDamage(int damage) {
        hp -= damage;
        if(hp <= 0) isEated = true;
    }

    public boolean isEated() {
        return this.isEated;
    }

    public int getPlantCost(){
        return this.plantCost;
    }
    public void setPlantCost(int a){
        this.plantCost = a;
    }

    public abstract EntityType getType();

    public abstract void Update();

    public abstract void writeToStream(FileOutputStream stream) throws IOException;
}
