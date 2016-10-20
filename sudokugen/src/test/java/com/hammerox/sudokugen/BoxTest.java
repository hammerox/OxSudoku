package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 20-Oct-16.
 */

public class BoxTest {

    @Test
    public void positionBoxRelation() {
        byte[] box = Box.getBoxIndexes(2, 3);
        Assert.assertArrayEquals(box, Box.BOX_TOP_LEFT);
        byte[] indexBox = Box.getBoxIndexes(0);
        Assert.assertArrayEquals(indexBox, Box.BOX_TOP_LEFT);

        box = Box.getBoxIndexes(4, 3);
        Assert.assertArrayEquals(box, Box.BOX_CENTER_LEFT);
        indexBox = Box.getBoxIndexes(29);
        Assert.assertArrayEquals(indexBox, Box.BOX_CENTER_LEFT);

        box = Box.getBoxIndexes(9, 2);
        Assert.assertArrayEquals(box, Box.BOX_BOTTOM_LEFT);
        indexBox = Box.getBoxIndexes(73);
        Assert.assertArrayEquals(indexBox, Box.BOX_BOTTOM_LEFT);

        box = Box.getBoxIndexes(2, 4);
        Assert.assertArrayEquals(box, Box.BOX_TOP_CENTER);
        indexBox = Box.getBoxIndexes(12);
        Assert.assertArrayEquals(indexBox, Box.BOX_TOP_CENTER);

        box = Box.getBoxIndexes(4, 5);
        Assert.assertArrayEquals(box, Box.BOX_CENTER_CENTER);
        indexBox = Box.getBoxIndexes(31);
        Assert.assertArrayEquals(indexBox, Box.BOX_CENTER_CENTER);

        box = Box.getBoxIndexes(9, 6);
        Assert.assertArrayEquals(box, Box.BOX_BOTTOM_CENTER);
        indexBox = Box.getBoxIndexes(77);
        Assert.assertArrayEquals(indexBox, Box.BOX_BOTTOM_CENTER);
    }

}
