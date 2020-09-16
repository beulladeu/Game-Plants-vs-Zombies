package main.java.tests;

import main.java.model.*;
import main.java.model.util.Vector2D;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void searchSunsArray() {
        Game game = new Game(2, 2, 3,3, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
        Sun sun1 = new Sun(new Vector2D(200, 60), -2, 10000, true);
        Sun sun2 = new Sun(new Vector2D(530, 93), -2, 10000, true);

        game.sunsArrayAdd(sun1);
        game.sunsArrayAdd(sun2);

        Sun sunExpected = new Sun(new Vector2D(530, 93), -2, 10000, true);
        Assert.assertEquals(sunExpected, game.searchSunsArray(540, 100));
    }

    @Test
    public void plantSunflower() {
        Game game = new Game(2, 2, 3,3, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
        Sunflower sunflower = new Sunflower(new Vector2D(310, 230));

        Assert.assertEquals(true, game.plantSunflower(sunflower.getPosition()));
    }

    @Test
    public void plantSunflowerTwo() {
        Game game = new Game(2, 2, 3,3, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
        Sunflower sunflower = new Sunflower(new Vector2D(210, 130));
        game.getRoadMap().setBlock(sunflower.getPosition(), sunflower);

        Sunflower sunflowerActual = new Sunflower(new Vector2D(210, 130));

        Assert.assertEquals(false, game.plantSunflower(sunflowerActual.getPosition()));
    }

    @Test
    public void plantGunflower() {
        Game game = new Game(2, 2, 3,3, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
        game.setSunScore(200);
        Gunflower gunflower = new Gunflower(new Vector2D(10, 110));

        Assert.assertEquals(true, game.plantGunflower(gunflower.getPosition()));
    }

    @Test
    public void plantGunflowerTwo() {
        Game game = new Game(2, 2, 3,3, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
        game.setSunScore(200);
        Gunflower gunflower = new Gunflower(new Vector2D(210, 130));
        game.getRoadMap().setBlock(gunflower.getPosition(), gunflower);
        Gunflower gunflowerActual = new Gunflower(new Vector2D(210, 130));

        Assert.assertEquals(false, game.plantSunflower(gunflowerActual.getPosition()));
    }


    @Test
    public void loadGameFromFile() throws IOException {
        Game game = Game.loadGameFromFile("C:\\Users\\beulladeu\\IdeaProjects\\GameMillionVersion\\lastGameTest.bin");


        Game gameActual = new Game(0, 1, 3,7, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
        gameActual.isPaused = false;
        gameActual.setSunScore(50);

        Assert.assertEquals(game, gameActual);
    }
}