package main.java.model;

import main.java.model.util.ByteConverter;
import main.java.model.util.Vector2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Sunflower extends Flower {

    public int sunGenerator;

    public Sunflower(Vector2D position) {
        super(position);
        this.sunGenerator = 50;
        this.hp = Constant.SUN_FLOWER_HP;
        this.plantCost = Constant.SUN_FLOWER_COST;
    }


    public void Update(){
        this.sunGenerator--;
    }

    public void resetSunGenerator() {
        this.sunGenerator = 200;
    }

    public EntityType getType() {
        return EntityType.SunFlower;
    }


    public void writeToStream(FileOutputStream stream) throws IOException {
        position.writeToStream(stream);
        stream.write(ByteConverter.intToBytes(sunGenerator));
        stream.write(ByteConverter.intToBytes(hp));
        stream.write(ByteConverter.boolToBytes(isEated));
    }

    public static Sunflower readFromStream(FileInputStream stream) throws IOException {
        Sunflower sunflower = new Sunflower(Vector2D.readFromStream(stream));
        byte[] bytes = new byte[4];
        stream.read(bytes);
        sunflower.sunGenerator = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        sunflower.hp = ByteConverter.bytesToInt(bytes);
        sunflower.isEated = ByteConverter.byteToBool((byte)stream.read());
        return sunflower;
    }
}
