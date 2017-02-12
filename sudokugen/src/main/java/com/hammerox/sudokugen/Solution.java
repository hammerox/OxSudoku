package com.hammerox.sudokugen;

import java.util.Random;
import java.util.Set;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public class Solution extends Board {


    private Random random = new Random();


    public Solution() {
        set(randomIndex(), randomValue());

    }


    private int randomIndex() {
        return random.nextInt(80);
    }

    private int randomValue() {
        return random.nextInt(8) + 1;
    }

    private int randomValue(Set<Integer> availableValues) {
        while (true) {
            int pickedValue = randomValue();
            if (availableValues.contains(pickedValue)) {
                return pickedValue;
            }
        }
    }
}
