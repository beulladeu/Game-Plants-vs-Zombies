package main.java.tests;

import main.java.model.Constant;
import main.java.model.Map;
import main.java.model.Sunflower;
import main.java.model.util.Vector2D;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTest {

    @Test
    public void xTransformation() {
        Map map = new Map(5,3);
        Assert.assertEquals(2, map.xTransformation(203));
    }

    @Test
    public void yTransformation() {
        Map map = new Map(5,3);
        Assert.assertEquals(2, map.yTransformation(203));
    }



    @Test
    public void getNormalizedPos(){
        Map map = new Map(5,3);

        Assert.assertEquals(new Vector2D(210,110), map.getNormalizedPos(new Vector2D(203, 150)));
    }



    @Test
    public void getBlock(){
        Map map = new Map(5,3);
        Sunflower sunflower = new Sunflower(new Vector2D(310, 230));
        map.setBlock(new Vector2D(210, 130), sunflower);
        Assert.assertEquals(sunflower, map.getBlock(new Vector2D(210, 130)));
    }

}