package main.java.model;


import main.java.model.*;
import main.java.model.util.ByteConverter;
import main.java.model.util.Vector2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Map {

    private Flower map[][];
    private int rows;
    private int columns;

    public Map(int columns, int rows){
        this.rows = rows;
        this.columns = columns;
        map = new Flower[this.getColumns()][this.getRows()];
        for(int i = 0; i < this.getColumns(); i++){
            for(int j = 0; j < this.getRows(); j++){
                map[i][j] = null;
            }
        }
    }

    public Flower[][] getMap() {
        return map;
    }

    public int getRows(){
        return this.rows;
    }

    public int getColumns(){
        return this.columns;
    }

    public int xTransformation(int x){

        if(x < 0) return -1;

        int indexX = x / Constant.SIZE_POINT;

        if(indexX >= this.getColumns()) return  -1;

        return indexX;
    }

    public int yTransformation(int y){
        if(y < 0) return -1;

        int indexY = y / Constant.SIZE_POINT;

        if(indexY >= this.getRows()) return  -1;

        return indexY;
    }

    public Flower getBlock(Vector2D pos) {
        int indexX = xTransformation(pos.x);
        int indexY = yTransformation(pos.y);

        if (indexX >= this.getColumns() || indexY >= this.getRows() || indexX < 0 || indexY < 0) {
            return null;
        }

        return map[indexX][indexY];
    }

    public void setBlock(Vector2D pos, Flower flower){
        int indexX = xTransformation(pos.x);
        int indexY = yTransformation(pos.y);

        if(indexX >= this.getColumns() || indexY >= this.getRows() || indexX < 0 || indexY < 0){
            return;
        }

        map[indexX][indexY] = flower;
    }

    public Vector2D getNormalizedPos(Vector2D pos) {
        int indexX = xTransformation(pos.x);
        int indexY = yTransformation(pos.y);

        return new Vector2D(indexX * Constant.SIZE_POINT + (Constant.SIZE_POINT - Constant.SHOP_OBJECTS_SIZE) / 2,
                            indexY * Constant.SIZE_POINT + (Constant.SIZE_POINT - Constant.SHOP_OBJECTS_SIZE) / 2);
    }





    public void writeToStream(FileOutputStream stream) throws IOException {
        stream.write(ByteConverter.intToBytes(columns));
        stream.write(ByteConverter.intToBytes(rows));


        for(int i = 0; i < getColumns(); i++) {
            for (int j = 0; j < getRows(); j++) {
                if(map[i][j] != null) {
                    stream.write(ByteConverter.intToBytes(map[i][j].getType().ordinal()));
                } else {
                    stream.write(ByteConverter.intToBytes(-1));
                }
            }
        }

        for(int i = 0; i < getColumns(); i++) {
            for (int j = 0; j < getRows(); j++) {
                if (map[i][j] != null) {
                    map[i][j].writeToStream(stream);
                }
            }
        }
    }

    public static Map readFromStream(FileInputStream stream) throws IOException {
        byte[] bytes = new byte[4];
        stream.read(bytes);
        int columns = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        int rows = ByteConverter.bytesToInt(bytes);

        Map map = new Map(columns, rows);

        int[][] b = new int[columns][rows];
        for(int i = 0; i < map.getColumns(); i++) {
            for (int j = 0; j < map.getRows(); j++) {
                stream.read(bytes);
                b[i][j] = ByteConverter.bytesToInt(bytes);
            }
        }

        for(int i = 0; i < map.getColumns(); i++) {
            for (int j = 0; j < map.getRows(); j++) {
                if (b[i][j] == -1) {
                    map.map[i][j] = null;
                } else if (b[i][j] == Entity.EntityType.SunFlower.ordinal()) {
                    map.map[i][j] = Sunflower.readFromStream(stream);
                } else if (b[i][j] == Entity.EntityType.GunFlower.ordinal()) {
                    map.map[i][j] = Gunflower.readFromStream(stream);
                }
            }
        }

        return map;
    }
}
