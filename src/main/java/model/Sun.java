package main.java.model;

import main.java.model.Entity;
import main.java.model.util.ByteConverter;
import main.java.model.util.Vector2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Sun extends Entity {
    private int ySpeed;
    private boolean catchState;
    private boolean isFallSun;
    private int lifetime;

    private Vector2D startPosition = new Vector2D(0, 0);
    private Vector2D speed = new Vector2D(0, 0);
    private final Vector2D gravity = new Vector2D(0, -2);

    public Sun(Vector2D position, int speed, int lifetime, boolean isFallSun) {
        super(position);
        startPosition = position;
        this.ySpeed = speed;
        this.catchState = false;
        this.lifetime = lifetime;
        this.isFallSun = isFallSun;
        this.speed = new Vector2D(1 + (int) (8 * Math.random()), 4 + (int) (8 * Math.random()));
    }

    public boolean isCatchState() {
        return catchState;
    }

    public void setCatchSunState(boolean state) {
        this.catchState = state;
    }

    public void sunUpdate(){
        if(!isFallSun) {
            position = position.Add(speed);
            speed = speed.Add(gravity);
            if(position.y <= startPosition.y) speed = new Vector2D(0, 0);
        }
        lifetime -= 1;
        if(lifetime <= 0) catchState = true;
        this.position.y += ySpeed;
    }

    public EntityType getType() {
        return EntityType.Sun;
    }


    public void writeToStream(FileOutputStream stream) throws IOException {
        position.writeToStream(stream);
        startPosition.writeToStream(stream);
        speed.writeToStream(stream);

        stream.write(ByteConverter.intToBytes(ySpeed));
        stream.write(ByteConverter.intToBytes(lifetime));

        stream.write(ByteConverter.boolToBytes(catchState));
        stream.write(ByteConverter.boolToBytes(isFallSun));
    }

    public static Sun readFromStream(FileInputStream stream) throws IOException {

        Vector2D position = Vector2D.readFromStream(stream);
        Vector2D startPosition = Vector2D.readFromStream(stream);
        Vector2D speed = Vector2D.readFromStream(stream);

        byte[] bytes = new byte[4];
        stream.read(bytes);
        int ySpeed = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        int lifetime = ByteConverter.bytesToInt(bytes);

        boolean catchState = ByteConverter.byteToBool((byte)stream.read());
        boolean isFallSun = ByteConverter.byteToBool((byte)stream.read());

        Sun sun = new Sun(position, ySpeed, lifetime, isFallSun);
        sun.startPosition = startPosition;
        sun.speed = speed;
        sun.catchState = catchState;
        return sun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Sun inp = (Sun) o;
        if(this.ySpeed == inp.ySpeed && this.position.x == inp.position.x && this.position.y == inp.position.y
                && this.isFallSun == inp.isFallSun && this.catchState == inp.catchState){
            return true;
        }
        return false;
    }
}
