package main.java.model;

import main.java.model.*;
import main.java.model.util.ByteConverter;
import main.java.model.util.Vector2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Game {

    private final int levelNumber;
    private Map roadMap;
    private int zombieNumInLvl;
    private int xMapShift;
    private int yMapShift;
    private int sunScore;
    private boolean isWin;
    private boolean isLoser;

    public boolean isPaused = false;
    private boolean isStop = false;

    private int sunGeneration;
    private int zombieGeneration;
    private int sunflowerGeneration;
    private int gunflowerGeneration;

    private boolean sunflowerReady = false;
    private boolean gunflowerReady = false;

    private ArrayList<Sun> sunsArray;
    private ArrayList<Zombie> zombiesArray;
    private ArrayList<Flower> flowersArray;
    private ArrayList<Bomb> bombsArray;

    final private int savedZombieNumInLvl;
    final private int savedMapColumn;
    final private int savedMapRow;
    final private int savedXMapShift;
    final private int savedYMapShift;
    final private int savedSunScore = 50;
    final private int savedSunGeneration = 30;
    final private int savedZombieGeneration = 200;
    final private int savedSunflowerGeneration = 20;
    final private int savedGunflowerGeneration = 20;


    public Game(int levelNumber, int zombieNumInLvl, int mapRow, int mapColumn, int xMapShift, int yMapShift){
        this.levelNumber = levelNumber;
        this.savedMapColumn = mapColumn;
        this.savedMapRow = mapRow;
        this.savedZombieNumInLvl = zombieNumInLvl;
        this.savedXMapShift = xMapShift;
        this.savedYMapShift = yMapShift;
        this.restart();
    }


    //--------suns--------
    public void sunsArrayAdd(Sun obj){
        sunsArray.add(obj);
    }

    public Sun getSunsArray(int index){
        if(index >= sunsArray.size() || index < 0) return null;
        return sunsArray.get(index);
    }

    public void removeSunsArray(int index){
        sunsArray.remove(index);
    }

    public void catchSun(Sun sun) {
        sunScore = sunScore + Constant.SUN_COST;
        sunsArray.remove(sun);
    }

    public Sun searchSunsArray(int x, int y){
        for(int i = 0; i < sunsArray.size(); i++){
            boolean is_zone = x >= getSunsArray(i).position.x && x <= getSunsArray(i).position.x + Constant.SUN_SIZE && y >= getSunsArray(i).position.y && y <= getSunsArray(i).position.y + Constant.SUN_SIZE;
            if(is_zone){
                return getSunsArray(i);
            }
        }
        return null;
    }


    //--------zombies--------
    public void zombiesArrayAdd(Zombie obj){
        zombiesArray.add(obj);
    }

    public Zombie getZombiesArray(int index){
        if(index >= zombiesArray.size() || index < 0) return null;
        return zombiesArray.get(index);
    }

    public void removeZombiesArray(int index){
        zombiesArray.remove(index);
    }

    public void removeZombiesArray(Zombie zombie){
        zombiesArray.remove(zombie);
    }


    //--------flowers--------
    public void flowersArrayAdd(Flower obj){
        flowersArray.add(obj);
    }

    public Flower getFlowersArray(int index){
        if(index >= flowersArray.size() || index < 0) return null;
        return flowersArray.get(index);
    }

    public void removeFlowersArray(int index){
        flowersArray.remove(index);
    }

    public void removeFlowersArray(Flower flower){
        flowersArray.remove(flower);
    }

    //--------bombs--------
    public void bombsArrayAdd(Bomb obj){
        bombsArray.add(obj);
    }

    public Bomb getBombsArray(int index){
        if(index >= bombsArray.size() || index < 0) return null;
        return bombsArray.get(index);
    }

    public void removeBombArray(int index){
        bombsArray.remove(index);
    }

    public void removeBombArray(Bomb bomb){
        bombsArray.remove(bomb);
    }


    public boolean plantSunflower(Vector2D pos){
        if(sunScore < Constant.SUN_FLOWER_COST) return false;

        Flower flower = getRoadMap().getBlock(pos);
        pos = getRoadMap().getNormalizedPos(pos);
        if(flower == null) {
            Sunflower sunflower = new Sunflower(pos.Add(new Vector2D(getXMapShift(), getYMapShift())));
            flowersArray.add(sunflower);
            getRoadMap().setBlock(pos, sunflower);
            setIsSunflowerReady(false);
            sunScore -= Constant.SUN_FLOWER_COST;
            return true;
        }
        return false;
    }

    public boolean plantGunflower(Vector2D pos){
        if(sunScore < Constant.GUN_FLOWER_COST) return false;

        Flower flower = getRoadMap().getBlock(pos);

        pos = getRoadMap().getNormalizedPos(pos);

        if(flower == null) {
            Gunflower gunflower = new Gunflower(pos.Add(new Vector2D(getXMapShift(), getYMapShift())));
            flowersArray.add(gunflower);
            getRoadMap().setBlock(pos, gunflower);
            setIsGunflowerReady(false);
            sunScore -= Constant.GUN_FLOWER_COST;
            return true;
        }
        return false;
    }

    public void destroyFlower(Vector2D pos) {
        Flower flower = getRoadMap().getBlock(pos);

        if(flower != null) {
            getRoadMap().setBlock(pos, null);
            removeFlowersArray(flower);
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public Map getRoadMap() {
        return roadMap;
    }

    public void setRoadMap(Map roadMap) {
        this.roadMap = roadMap;
    }

    public boolean isWin(){
        return this.isWin;
    }

    public boolean isLoser() {
        return isLoser;
    }

    public int getSunScore(){
        return sunScore;
    }

    public void setSunScore(int score){
        sunScore += score;
    }

    public int getRowMap(){
        return getRoadMap().getRows();
    }

    public int getColumnMap(){
        return getRoadMap().getColumns();
    }

    public boolean getIsSunflowerReady(){
        return sunflowerReady;
    }

    public void setIsSunflowerReady(boolean state){
        sunflowerReady = state;
    }

    public boolean getIsGunflowerReady(){
        return gunflowerReady;
    }

    public void setIsGunflowerReady(boolean state){
        gunflowerReady = state;
    }

    public boolean isStop() {
        return isStop;
    }

    public void stop() {
        isStop = true;
    }

    public int getXMapShift(){
        return this.xMapShift;
    }

    public int getYMapShift(){
        return this.yMapShift;
    }


    public void restart() {
        this.sunScore = this.savedSunScore;
        this.zombieNumInLvl = this.savedZombieNumInLvl;
        this.setRoadMap(new Map(savedMapColumn, savedMapRow));
        this.sunsArray = new ArrayList<>();
        this.zombiesArray = new ArrayList<>();
        this.flowersArray = new ArrayList<>();
        this.bombsArray = new ArrayList<>();
        this.isWin = false;
        this.isLoser = false;
        this.isPaused = false;
        this.isStop = false;
        this.xMapShift = this.savedXMapShift;
        this.yMapShift = this.savedYMapShift;
        this.sunGeneration = savedSunGeneration;
        this.zombieGeneration = this.savedZombieGeneration;
        this.sunflowerGeneration = this.savedSunflowerGeneration;
        this.gunflowerGeneration = this.savedGunflowerGeneration;
    }


    public void gameUpdates(){

        if(isPaused) return;

        if(zombieNumInLvl == 0 && zombiesArray.isEmpty()){
            this.isPaused = true;
            this.isWin = true;

        }

        for (int i = 0; i < sunsArray.size(); i++){//удаляет солнышко, которое вышло за фрейм
            if(getSunsArray(i).position.y < Constant.RED_ZONE_Y || getSunsArray(i).isCatchState()){
                removeSunsArray(i);
            }
        }

        //generation fall suns
        sunGeneration--;
        if(sunGeneration == 0){
            Vector2D pos = new Vector2D((int) (Math.random() * (Constant.WIDTH_FRAME - Constant.SUN_SIZE-xMapShift)), Constant.SUN_SKY_Y_SPAWN);
            Sun sun = new Sun(pos, -2, 10000, true);
            sunsArray.add(sun);
            sunGeneration = 200;
        }


        //zombie generation
        zombieGeneration--;
        if(zombieGeneration == 0 && zombieNumInLvl > 0){
            System.out.println("zombie");
            int yGen =  (int) (Math.random() * getRoadMap().getRows());
            Zombie zombie = new Zombie(new Vector2D(Constant.WIDTH_FRAME + Constant.ZOMBIE_SPAWN_FRAME_OUT, yGen*Constant.SIZE_POINT + Constant.ZOMBIE_SIZE + yMapShift));
            zombiesArray.add(zombie);
            zombieNumInLvl--;
            zombieGeneration = 200 + (int) (Math.random() * 500);
        }


        //sunflower cooldown
        sunflowerGeneration--;
        if(sunflowerGeneration <= 0 && this.sunScore >= Constant.SUN_FLOWER_COST && !sunflowerReady){
            System.out.println("sunflower");
            sunflowerReady = true;
            sunflowerGeneration = 100;
        }
        if(sunScore < Constant.SUN_FLOWER_COST) {
            sunflowerReady = false;
        }

        //gunflower cooldown
        gunflowerGeneration--;
        if(gunflowerGeneration <= 0 && this.sunScore >= Constant.GUN_FLOWER_COST && !gunflowerReady){
            System.out.println("gunflower");
            gunflowerReady = true;
            gunflowerGeneration = 150;
        }
        if(sunScore < Constant.GUN_FLOWER_COST) {
            gunflowerReady = false;
        }


        //generation sun around sunflowers
        for (int i = 0; i < flowersArray.size(); i++) {
            if (flowersArray.get(i).getClass() == Sunflower.class) {
                Sunflower sunflower = (Sunflower)flowersArray.get(i);
                if (sunflower.sunGenerator == 0) {
                    Sun sun = new Sun(sunflower.position.Add(new Vector2D(30, 30)), 0, 300, false);
                    sunsArrayAdd(sun);
                    sunflower.resetSunGenerator();
                }
            }
        }


        for (int i = 0; i < zombiesArray.size(); i++) {//eating flower
            Zombie zombie = getZombiesArray(i);
            Vector2D position = zombie.position;
            position = position.Sub(new Vector2D(0, Constant.ZOMBIE_SIZE / 2));
            position = position.Sub(new Vector2D(getXMapShift(), getYMapShift()));


            Flower flower = getRoadMap().getBlock(position);
            if (flower != null) {
                System.out.println("eat zone");
                if(zombie.isZombieReady()) {
                    zombie.attack();
                } else if(zombie.isZombieDealDamage()) {
                    flower.consumeDamage(zombie.getDamage());
                }
                System.out.println(flower.hp);
                if (flower.isEated()) {//если съедено, то идти дальше
                    getRoadMap().setBlock(flower.position.Sub(new Vector2D(getXMapShift(), getYMapShift())), null);
                    removeFlowersArray(flower);
                }
            }
            else if(zombie.isState() == Zombie.ZombieState.wait) zombie.setZombieState(Zombie.ZombieState.walk);
        }



        for (int i = 0; i < zombiesArray.size(); i++){
            if(getZombiesArray(i).position.x < Constant.RED_ZONE_X){
                System.out.println("zombie in red zone");
                this.isLoser = true;
                this.isPaused = true;
                removeZombiesArray(i);
            }
        }

        for (int i = 0; i < bombsArray.size(); i++) {
            if (getBombsArray(i).position.x > Constant.WIDTH_FRAME) {
                removeBombArray(i);
            } else {
                for (int j = 0; j < zombiesArray.size(); j++) {
                    if (getRoadMap().yTransformation(getBombsArray(i).position.y - yMapShift) == getRoadMap().yTransformation(getZombiesArray(j).position.y - Constant.ZOMBIE_SIZE / 2 - yMapShift)) {
                        if(getZombiesArray(j).position.x - getBombsArray(i).position.x < 0) {
                            getZombiesArray(j).consumeDamage(getBombsArray(i).getDamage());
                            removeBombArray(i);
                            if(getZombiesArray(j).getHP() <= 0) {
                                removeZombiesArray(j);
                                j--;
                            }
                            i--;
                            break;
                        }
                    }
                }
            }
        }


        for (Sun sun : sunsArray) {
            sun.sunUpdate();
        }

        for (Zombie zombie : zombiesArray) {
            zombie.zombieUpdate();
            if(!zombie.inFrame && zombie.position.x <= Constant.WIDTH_FRAME) {
                zombie.inFrame = true;
            }
        }

        for (Flower flower : flowersArray) {
            if(flower.getClass() == Gunflower.class) {
                boolean flag = false;
                for (Zombie zombie : zombiesArray) {
                    if (zombie.inFrame && flower.position.x < zombie.position.x && getRoadMap().yTransformation(flower.position.y - yMapShift) == getRoadMap().yTransformation(zombie.position.y - Constant.ZOMBIE_SIZE / 2 - yMapShift)) {
                        flag = true;
                        break;
                    }
                }
                Gunflower gunflower = (Gunflower) flower;
                if (gunflower.getState() != Gunflower.GunFlowerState.cooldown) {
                    gunflower.setState(flag ? Gunflower.GunFlowerState.fire : Gunflower.GunFlowerState.wait);
                    if (gunflower.getState() == Gunflower.GunFlowerState.fire) {
                        Bomb bomb = new Bomb(gunflower.position.Add(new Vector2D()));
                        bombsArrayAdd(bomb);
                    }
                }
            }
            flower.Update();
        }

        for (Bomb bomb : bombsArray) {
            bomb.bombUpdate();
        }
    }


    public void saveGameToFile(String filename) throws IOException {
        FileOutputStream stream = new FileOutputStream(filename);

        stream.write(ByteConverter.intToBytes(levelNumber));
        stream.write(ByteConverter.intToBytes(savedZombieNumInLvl));
        stream.write(ByteConverter.intToBytes(savedMapColumn));
        stream.write(ByteConverter.intToBytes(savedMapRow));
        stream.write(ByteConverter.intToBytes(savedXMapShift));
        stream.write(ByteConverter.intToBytes(savedYMapShift));

        roadMap.writeToStream(stream);

        stream.write(ByteConverter.intToBytes(zombieNumInLvl));
        stream.write(ByteConverter.intToBytes(sunScore));
        stream.write(ByteConverter.intToBytes(sunGeneration));
        stream.write(ByteConverter.intToBytes(zombieGeneration));
        stream.write(ByteConverter.intToBytes(sunflowerGeneration));
        stream.write(ByteConverter.intToBytes(gunflowerGeneration));


        stream.write(ByteConverter.boolToByte(sunflowerReady));
        stream.write(ByteConverter.boolToByte(gunflowerReady));

        stream.write(ByteConverter.intToBytes(sunsArray.size()));
        for (int i = 0; i < sunsArray.size(); i++) {
            sunsArray.get(i).writeToStream(stream);
        }

        stream.write(ByteConverter.intToBytes(zombiesArray.size()));
        for (int i = 0; i < zombiesArray.size(); i++) {
            zombiesArray.get(i).writeToStream(stream);
        }

        stream.write(ByteConverter.intToBytes(bombsArray.size()));
        for (int i = 0; i < bombsArray.size(); i++) {
            bombsArray.get(i).writeToStream(stream);
        }
    }

    public static Game loadGameFromFile(String filename) throws IOException {
        if(!new File(filename).exists()) return null;

        FileInputStream stream = new FileInputStream(filename);

        int levelNumber, savedZombieNumInLvl, savedMapColumn, savedMapRow, savedXMapShift, savedYMapShift;
        byte[] bytes = new byte[4];
        stream.read(bytes);
        levelNumber = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        savedZombieNumInLvl = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        savedMapColumn = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        savedMapRow = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        savedXMapShift = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        savedYMapShift = ByteConverter.bytesToInt(bytes);


        Game game = new Game(levelNumber, savedZombieNumInLvl, savedMapRow, savedMapColumn, savedXMapShift, savedYMapShift);

        game.roadMap = Map.readFromStream(stream);

        stream.read(bytes);
        game.zombieNumInLvl = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        game.sunScore = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        game.sunGeneration = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        game.zombieGeneration = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        game.sunflowerGeneration = ByteConverter.bytesToInt(bytes);
        stream.read(bytes);
        game.gunflowerGeneration = ByteConverter.bytesToInt(bytes);


        game.sunflowerReady = ByteConverter.byteToBool((byte)stream.read());
        game.gunflowerReady = ByteConverter.byteToBool((byte)stream.read());

        stream.read(bytes);
        int sunsSize = ByteConverter.bytesToInt(bytes);
        for (int i = 0; i < sunsSize; i++) {
            game.sunsArray.add(Sun.readFromStream(stream));
        }

        stream.read(bytes);
        int zombiesSize = ByteConverter.bytesToInt(bytes);
        for (int i = 0; i < zombiesSize; i++) {
            game.zombiesArray.add(Zombie.readFromStream(stream));
        }

        stream.read(bytes);
        int bombsSize = ByteConverter.bytesToInt(bytes);
        for (int i = 0; i < bombsSize; i++) {
            game.bombsArray.add(Bomb.readFromStream(stream));
        }

        for (int i = 0; i < savedMapColumn; i++) {
            for (int j = 0; j < savedMapRow; j++) {
                if(game.roadMap.getMap()[i][j] != null) {
                    game.flowersArray.add(game.roadMap.getMap()[i][j]);
                }
            }
        }

        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Game game = (Game) o;
        if(this.getLevelNumber() == game.getLevelNumber() && this.isPaused == game.isPaused && this.isWin == game.isWin
                && this.isLoser == game.isLoser && this.zombieNumInLvl == game.zombieNumInLvl
                && this.flowersArray.equals(game.flowersArray)) {
            return true;
        }
        return false;
    }
}
