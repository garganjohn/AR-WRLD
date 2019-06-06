package org.pursuit.ar_wrld.movement;

import java.util.Random;

public class ModelCoordinates {
    //Random X coordinates will be between -.2 to .4f
    //Random Y coordinates will be between -.5 to .5
    public float randomCoordinates(boolean isX) {
        Random random = new Random();

        if (isX) {
            float min = -.2f;
            float max = .4f;
            return (min + random.nextFloat() * (max - min));
        }

        return random.nextFloat() - .500f;
    }

    // Number is displayed between -.7 and -1
    public float randomZCoordinates() {
//        Random random = new Random();
//        Float minFloat = .7f;
//        Float maxFloat = 1f;
        //Location behind user
//        if (new Random().nextInt(2) == 0) {
//            return minFloat + random.nextFloat() * (maxFloat - minFloat);
//        }
        //Location infront of user
//        return -(minFloat + random.nextFloat() * (maxFloat - minFloat));
        if (new Random().nextFloat() > .8f) return -3f;
        return 3f;
    }
}
