package main.java.model;

import main.java.model.util.ByteConverter;
import main.java.model.util.Vector2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Gunflower extends Flower{

    private GunFlowerState state = GunFlowerState.wait;

    private int startCooldown = 100;
    private int cooldown = 0;

    public Gunflower(Vector2D position) {
        super(position);
        this.plantCost = Constant.GUN_FLOWER_COST;
        this.hp = Constant.GUN_FLOWER_HP;
    }

    public void Update() {
        if (state == GunFlowerState.wait) {

        } else if(state == GunFlowerState.fire) {
            cooldown = startCooldown;
            state = GunFlowerState.cooldown;
        } else if(state == GunFlowerState.cooldown) {
            if(cooldown == 0) state = GunFlowerState.fire;
        }
        if(cooldown > 0) cooldown--;
    }

    public GunFlowerState getState() {
        return this.state;
    }

    public void setState(GunFlowerState state) {
        this.state = state;
    }

    public EntityType getType() {
        return EntityType.GunFlower;
    }

    public enum GunFlowerState {
        wait,
        fire,
        cooldown,
    };


    public void writeToStream(FileOutputStream stream) throws IOException {
        position.writeToStream(stream);
        stream.write(ByteConverter.intToBytes(state.ordinal()));
        stream.write(ByteConverter.intToBytes(cooldown));
        stream.write(ByteConverter.intToBytes(hp));
        stream.write(ByteConverter.boolToBytes(isEated));
    }

    public static Gunflower readFromStream(FileInputStream stream) throws IOException {
        Gunflower gunflower = new Gunflower(Vector2D.readFromStream(stream));
        byte[] bytes = new byte[4];
        gunflower.state = GunFlowerState.values()[ByteConverter.bytesToInt(bytes)];
        stream.read(bytes);
        gunflower.cooldown = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        gunflower.hp = ByteConverter.bytesToInt(bytes);
        gunflower.isEated = ByteConverter.byteToBool((byte)stream.read());
        return gunflower;
    }
}
