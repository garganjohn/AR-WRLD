package org.toocoolforschool979.ar_wrld;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

//    @Test
//    public void isRandomX_correct_when_used() {
//        boolean isExpected;
//        for (int i = 0; i < 100; i++) {
//
//            float result = MainActivity.randomCoordinates(true);
//            if (result >= -.30000000000f && result <= .3000000000f) {
//                isExpected = true;
//            } else {
//                isExpected = false;
//            }
////            assertTrue(isExpected);
//            assertEquals(result, .300f, 1);
//        }
//    }
    @Test
    public void isRandomZMethod_correct_when_used(){
        boolean isExpected = true;
        //.25 >= -1
        //.25 <= -.7
        for (int i = 0; i < 100; i++) {
            float result = MainActivity.randomZCoordinates();
            if (result >= 0){
                isExpected = false;
            }
            System.out.println(result);
            assertTrue("Number is not between .7 and 1", isExpected);
        }
    }

    @Test
    public void isRandomX_correct_number(){
        float min = -.1f;
        float max = 1.2f;
        Random random = new Random();
        boolean isExpected = true;

        for (int i = 0; i < 100; i++){
            float randomFloat = min + random.nextFloat() * (max - min);
            if (!(randomFloat <= max && randomFloat >= min)){
                isExpected = false;
            }
            assertTrue("Number is not between -1.f and 1.2f", isExpected);
            System.out.println(randomFloat);
        }
    }
}