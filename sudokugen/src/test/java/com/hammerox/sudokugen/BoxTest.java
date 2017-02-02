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
        Assert.assertArrayEquals(box, Box.TOP_LEFT.index);
        byte[] indexBox = Box.getBoxIndexes(0);
        Assert.assertArrayEquals(indexBox, Box.TOP_LEFT.index);

        box = Box.getBoxIndexes(4, 3);
        Assert.assertArrayEquals(box, Box.CENTER_LEFT.index);
        indexBox = Box.getBoxIndexes(29);
        Assert.assertArrayEquals(indexBox, Box.CENTER_LEFT.index);

        box = Box.getBoxIndexes(9, 2);
        Assert.assertArrayEquals(box, Box.BOTTOM_LEFT.index);
        indexBox = Box.getBoxIndexes(73);
        Assert.assertArrayEquals(indexBox, Box.BOTTOM_LEFT.index);

        box = Box.getBoxIndexes(2, 4);
        Assert.assertArrayEquals(box, Box.TOP_CENTER.index);
        indexBox = Box.getBoxIndexes(12);
        Assert.assertArrayEquals(indexBox, Box.TOP_CENTER.index);

        box = Box.getBoxIndexes(4, 5);
        Assert.assertArrayEquals(box, Box.CENTER_CENTER.index);
        indexBox = Box.getBoxIndexes(31);
        Assert.assertArrayEquals(indexBox, Box.CENTER_CENTER.index);

        box = Box.getBoxIndexes(9, 6);
        Assert.assertArrayEquals(box, Box.BOTTOM_CENTER.index);
        indexBox = Box.getBoxIndexes(77);
        Assert.assertArrayEquals(indexBox, Box.BOTTOM_CENTER.index);
    }

}
