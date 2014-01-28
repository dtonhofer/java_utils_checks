package com.mplify.checkers.tests;

import java.util.Random;

import org.junit.Test;

import com.mplify.checkers.Check;

@SuppressWarnings("static-method")
public class Junit_VarArgPerformance {

    private static Random rand = new Random();

    private static String makeRandomString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            buf.append(Character.charCount('A' + rand.nextInt(32)));
        }
        return buf.toString();
    }

    @Test
    public void testIsTrue_fixedParametersVsVarargs() {
        for (int round = 0; round < 60; round++) {
            int lim = 10;
            String[] rs = new String[lim];
            for (int c = 0; c < lim; c++) {
                int n = 100000;
                long start = System.currentTimeMillis();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < lim; j++) {
                        rs[j] = makeRandomString();
                    }
                    switch (c) {
                    case 0:
                        Check.isTrue(true, "msg");
                        break;
                    case 1:
                        Check.isTrue(true, "msg", rs[0]);
                        break;
                    case 2:
                        Check.isTrue(true, "msg", rs[0], rs[1]);
                        break;
                    case 3:
                        Check.isTrue(true, "msg", rs[0], rs[1], rs[2]);
                        break;
                    case 4:
                        Check.isTrue(true, "msg", rs[0], rs[1], rs[2], rs[3]);
                        break;
                    case 5:
                        Check.isTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4]);
                        break;
                    case 6:
                        Check.isTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5]);
                        break;
                    case 7:
                        Check.isTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5], rs[6]);
                        break;
                    case 8:
                        Check.isTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5], rs[6], rs[7]);
                        break;
                    case 9:
                        Check.isTrue(true, "msg", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5], rs[6], rs[7], rs[8]);
                        break;
                    default:
                        Check.cannotHappen();
                    }
                }
                long delta = System.currentTimeMillis() - start;
                // this takes around 2 ms
                System.out.println("Round " + round + ": Time taken for " + n + " " + c + "-argument calls: " + delta + " ms");
            }
        }
    }
}
