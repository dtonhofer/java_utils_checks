package com.example.tests;

import java.util.Random;

import org.junit.Test;

import com.example.BasicChecks;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Not a real test case, just something to quickly/naively verify how fast vararg
 * invocations are relative to mutiple-argument methods.
 * 
 * Turns out there is actually no real difference:
 * 
 * Total time taken for 5 rounds of 100000 0-argument calls: 34 ms
 * Total time taken for 5 rounds of 100000 1-argument calls: 28 ms
 * Total time taken for 5 rounds of 100000 2-argument calls: 27 ms
 * Total time taken for 5 rounds of 100000 3-argument calls: 71 ms
 * Total time taken for 5 rounds of 100000 4-argument calls: 19 ms
 * Total time taken for 5 rounds of 100000 5-argument calls: 19 ms
 * Total time taken for 5 rounds of 100000 6-argument calls: 13 ms
 * Total time taken for 5 rounds of 100000 7-argument calls: 19 ms
 * Total time taken for 5 rounds of 100000 8-argument calls: 19 ms
 * Total time taken for 5 rounds of 100000 9-argument calls: 21 ms
 ******************************************************************************/

@SuppressWarnings("static-method")
public class JUnit_VarArgPerformance {

    private static Random rand = new Random();

    private static String makeRandomString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            buf.append(Character.charCount('A' + rand.nextInt(32)));
        }
        return buf.toString();
    }

    @Test
    public void testIsTrue_fixedParametersVsVarargs() {
        int lim = 10;
        int n = 100000;
        int rounds = 5;
        long[] totals = new long[lim];
        for (int round = 0; round < rounds; round++) {
            //
            // Set up random strings to pass
            //
            String[] rs = new String[lim];
            for (int j = 0; j < lim; j++) {
                rs[j] = makeRandomString();
            }
            for (int c = 0; c < lim; c++) {
                //
                // Make "n" calls to isTrue() with varying number of arguments
                //                
                long start = System.currentTimeMillis();
                for (int i = 0; i < n; i++) {
                    switch (c) {
                    case 0:
                        BasicChecks.checkTrue(true, "msg");
                        break;
                    case 1:
                        BasicChecks.checkTrue(true, "msg", rs[0]);
                        break;
                    case 2:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1]);
                        break;
                    case 3:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1], rs[2]);
                        break;
                    case 4:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1], rs[2], rs[3]);
                        break;
                    case 5:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4]);
                        break;
                    case 6:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5]);
                        break;
                    case 7:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5], rs[6]);
                        break;
                    case 8:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5], rs[6], rs[7]);
                        break;
                    case 9:
                        BasicChecks.checkTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5], rs[6], rs[7], rs[8]);
                        break;
                    default:
                        BasicChecks.cannotHappen();
                    }
                }                
                totals[c] += (System.currentTimeMillis() - start);
            }
        }
        for (int c = 0; c < lim; c++) {            
            System.out.println("Total time taken for " + rounds + " rounds of " + n + " " + c + "-argument calls: " + totals[c] + " ms");
        }
    }
}
