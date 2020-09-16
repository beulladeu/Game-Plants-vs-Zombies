package main.java.model;

import main.java.model.util.ByteConverter;
import main.java.model.util.Vector2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Zombie extends Entity{

    private ZombieState state;
    public boolean inFrame = false;
    private int xSpeed;
    private int hp;
    private int damage = 1;

    private int startCooldown = 30;
    private int cooldown = 0;

    private Vector2D startPosition = new Vector2D();
    private int animationTime = 10;
    private int time = 0;
    private int distance = 10;
    private int jumpHeight = (int)((0 - (float)animationTime / 4) * (0 - (float)animationTime / 4) * 1);
    private float animCoef = 1;


    public Zombie(Vector2D pos){
        super(pos);
        this.state = ZombieState.walk;
        this.hp = 5;
    }

    private void move(){
        this.position.x += this.xSpeed;
    }

    public ZombieState isState() {
        return state;
    }

    public void setZombieState(ZombieState state) {
        this.state = state;
    }

    public void zombieUpdate(){
        time = startCooldown - cooldown;
        if (state == ZombieState.walk) {
            this.xSpeed = -1;
            move();
        } else if(state == ZombieState.attack) {
            position.y = startPosition.y;
            if (time <= animationTime / 2) {
                position.x = startPosition.x - (int) (distance * ((float)time / (animationTime / 2)));
            }
            if (time > animationTime / 2) {
                state = ZombieState.dealDamage;
            }
        } else if(state == ZombieState.dealDamage) {
            state = ZombieState.cooldown;
        } else if(state == ZombieState.cooldown) {
            if (time > animationTime / 2) {
                position.x = startPosition.x + (int) (distance * ((float)time - animationTime / 2) / ((float)animationTime / 2));

            }
            if (time > animationTime) {
                position = startPosition;
                state = ZombieState.wait;
            }

        } else if(state == ZombieState.wait) {

        }

        if(cooldown > 0) cooldown--;
    }

    public boolean isZombieReady() {
        return cooldown == 0;
    }

    public boolean isZombieDealDamage() {
        return state == ZombieState.dealDamage;
    }

    public void attack() {
        this.startPosition = position;
        state = ZombieState.attack;
        cooldown = startCooldown;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getHP() {
        return hp;
    }

    public void consumeDamage(int damage) {
        hp -= damage;
    }

    public EntityType getType() {
        return EntityType.Zombie;
    }


    public enum ZombieState {
        walk,
        cooldown,
        wait,
        attack,
        dealDamage,
    }


    public void writeToStream(FileOutputStream stream) throws IOException {
        position.writeToStream(stream);
        startPosition.writeToStream(stream);
        stream.write(ByteConverter.intToBytes(state.ordinal()));
        stream.write(ByteConverter.intToBytes(hp));
        stream.write(ByteConverter.intToBytes(cooldown));
        stream.write(ByteConverter.intToBytes(time));
        stream.write(ByteConverter.boolToBytes(inFrame));
    }

    public static Zombie readFromStream(FileInputStream stream) throws IOException {
        Zombie zombie = new Zombie(Vector2D.readFromStream(stream));
        zombie.startPosition.readFromStream(stream);
        byte[] bytes = new byte[4];
        stream.read(bytes);
        zombie.state = ZombieState.values()[ByteConverter.bytesToInt(bytes)];
        stream.read(bytes);
        zombie.hp = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        zombie.cooldown = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        zombie.time = ByteConverter.bytesToInt(bytes);
        zombie.inFrame = ByteConverter.byteToBool((byte)stream.read());
        return zombie;
    }
}

