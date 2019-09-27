package org.pursuit.ar_wrld.movement;

import java.util.Random;

public class ModelCoordinates {
    public float randomCoordinates(boolean isX) {
        Random random = new Random();

        if (isX) {
            float min = -3f;
            float max = 3f;
            return (min + random.nextFloat() * (max - min));
        }

        return random.nextFloat() - .500f;
    }

    public float randomZCoordinates() {
        if (new Random().nextInt(9) > 8) return 3f;
        return -3f;
    }
}

