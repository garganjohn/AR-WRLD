package org.pursuit.ar_wrld;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

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
        boolean isExpected;
        for (int i = 0; i < 100; i++) {
            float result = MainActivity.randomZCoordinates();
            if (-1f <=result && result <= -.7f){
                isExpected = true;
            }
            else{
                isExpected = false;
            }
            System.out.println(result);
            assertTrue("Number is not between .7 and 1", isExpected);
        }
    }
}