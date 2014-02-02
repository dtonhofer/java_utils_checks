package com.example.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.example.CheckFailedException;
import com.example.BasicChecks;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Testing "BasicChecks"
 * 
 * 2014.01.22 - Created to test the correct behaviour of "Check" methods
 ******************************************************************************/

@SuppressWarnings("static-method")
public class JUnit_Check {

    @Test
    public void testCheckTrue_noMessage_andTrue() {
        BasicChecks.checkTrue(true);
    }

    @Test
    public void testCheckTrue_noMessage() {
        try {
            BasicChecks.checkTrue(false);
        } catch (CheckFailedException exe) {
            assertEquals("Test for 'true' fails (no further indication or text)", exe.getMessage());
        }
    }

    @Test
    public void testCeckTrue_zeroParameterMessage_andTrue() {
        BasicChecks.checkTrue(true, "message");
    }

    @Test
    public void testCheckTrue_zeroParameterMessage() {
        String msg = "message";
        try {
            BasicChecks.checkTrue(false, msg);
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @Test
    public void testCheckTrue_zeroParameterMessageThatIsMultiline() {
        String msg = "message line 1\nmessage line 2\n";
        try {
            BasicChecks.checkTrue(false, msg);
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @Test
    public void testCheckTrue_zeroParameterMessageThatIsNull() {
        try {
            BasicChecks.checkTrue(false, null);
        } catch (CheckFailedException exe) {
            assertEquals(null, exe.getMessage());
        }
    }

    @Test
    public void testCheckTrue_zeroParameterMessageWithOnePlaceholder() {
        String msg = "Placeholder {}";
        try {
            BasicChecks.checkTrue(false, msg);
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testCheckTrue_oneParameterMessage_andTrue() {
        BasicChecks.checkTrue(true, "one placeholder: {}", Integer.MAX_VALUE);
    }

    @SuppressWarnings("boxing")
    @Test
    public void testCheckTrue_oneParameterMessage() {
        try {
            BasicChecks.checkTrue(false, "one placeholder: {}", Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("one placeholder: 2147483647", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testCheckTrue_oneParameterMessageThatIsNull() {
        try {
            BasicChecks.checkTrue(false, null, Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("The format string was (null). The passed 1 arguments are: '2147483647'", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testCheckTrue_oneParameterMessageWithTwoPlaceholders() {
        try {
            BasicChecks.checkTrue(false, "two placeholders: {} and {}", Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testCheckTrue_oneParameterMessageWithTwoPlaceholdersOfDecimalType() {
        try {
            BasicChecks.checkTrue(false, "two placeholders: %d and %2d", Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
        }
    }

    @Test
    public void testCheckTrue_oneParameterMessageWithPlaceholderOfBadType() {
        try {
            BasicChecks.checkTrue(false, "one bad placeholders: %d", "WTF");
        } catch (CheckFailedException exe) {
            assertTrue(exe.getMessage().startsWith("Exception 'java.util.IllegalFormatConversionException' occurred during formatting of log message"));
        }
    }

    @Test
    public void testCheckTrue_oneParameterMessageWithNullParameter() {
        try {
            BasicChecks.checkTrue(false, "one placeholder: {}", null);
        } catch (CheckFailedException exe) {
            assertEquals("one placeholder: null", exe.getMessage());
        }
    }

    @Test(expected = CheckFailedException.class)
    public void testCheckNotNullAndNotOnlyWhitespace0() {
        BasicChecks.checkNotNullAndNotOnlyWhitespace(null, "bad string 0");
    }

    @Test(expected = CheckFailedException.class)
    public void testCheckNotNullAndNotOnlyWhitespace1() {
        BasicChecks.checkNotNullAndNotOnlyWhitespace("", "bad string 1");
    }

    @Test(expected = CheckFailedException.class)
    public void testCheckNotNullAndNotOnlyWhitespace2() {
        BasicChecks.checkNotNullAndNotOnlyWhitespace("    ", "bad string 2");
    }

    @Test
    public void testCheckNotNullAndNotOnlyWhitespace3() {
        BasicChecks.checkNotNullAndNotOnlyWhitespace("  XX  ", "good string");
    }

    @Test
    public void testCannotHappen() {
        for (int i = 0; i <= 5; i++) {
            try {
                switch (i) {
                case 0:
                    BasicChecks.cannotHappen();
                    break;
                case 1:
                    BasicChecks.cannotHappen("Random Text...");
                    break;
                case 2:
                    BasicChecks.cannotHappen(new IllegalArgumentException());
                    break;
                case 3:
                    BasicChecks.cannotHappen("Random Text...", new IllegalArgumentException());
                    break;
                case 4:
                    BasicChecks.cannotHappen("Looks {} it {}...", "like", "happened");
                    break;
                case 5:
                    BasicChecks.cannotHappen("Looks {} it {}...", new IllegalArgumentException(), "like", "happened");
                    break;
                default:
                    fail("Never get here");
                }
                fail("There should be an Error");
            } catch (Error err) {
                System.out.println(err.getMessage());
            }
        }
    }

    @Test
    public void testInstaFail() {
        for (int i = 0; i <= 2; i++) {
            try {
                switch (i) {
                case 0:
                    BasicChecks.instaFail();
                    break;
                case 1:
                    BasicChecks.instaFail("Check fails hard because of reasons");
                    break;
                case 2:
                    BasicChecks.instaFail("Looks {} check {}...", "like", "failed");
                    break;
                default:
                    fail("Never get here");
                }
                fail("There should be an Exception");
            } catch (CheckFailedException exe) {
                System.out.println(exe.getMessage());
            }
        }
    }

    @Test
    public void testCheckNotNull() {
        for (int i = 0; i <= 2; i++) {
            try {
                switch (i) {
                case 0:
                    BasicChecks.checkNotNull(null);
                    break;
                case 1:
                    BasicChecks.checkNotNull(null, null);
                    break;
                case 2:
                    BasicChecks.checkNotNull(null, "My Little Null Pony");
                    break;
                default:
                    fail("Never get here");
                }
                fail("There should be an Exception");
            } catch (CheckFailedException exe) {
                System.out.println(exe.getMessage());
            }
        }
        BasicChecks.checkNotNull("Not null");
        BasicChecks.checkNotNull("Not null", null);
        BasicChecks.checkNotNull("Not null", "My Little Non-Null Pony");
    }

    @Test
    public void testCheckNotNullAndNotEmpty() {
        for (int i = 0; i <= 13; i++) {
            try {
                switch (i) {
                case 0:
                    BasicChecks.checkNotNullAndNotEmpty(null);
                    break;
                case 1:
                    BasicChecks.checkNotNullAndNotEmpty(new ArrayList<Integer>());
                    break;
                case 2:
                    BasicChecks.checkNotNullAndNotEmpty(new ArrayList<Integer>(), "My List");
                    break;
                case 3:
                    BasicChecks.checkNotNullAndNotEmpty(new HashMap<Integer, Integer>());
                    break;
                case 4:
                    BasicChecks.checkNotNullAndNotEmpty(new HashMap<Integer, Integer>(), "My Map");
                    break;
                case 5:
                    BasicChecks.checkNotNullAndNotEmpty(new HashSet<Integer>());
                    break;
                case 6:
                    BasicChecks.checkNotNullAndNotEmpty(new HashSet<Integer>(), "My Set");
                    break;
                case 7:
                    BasicChecks.checkNotNullAndNotEmpty("");
                    break;
                case 8:
                    BasicChecks.checkNotNullAndNotEmpty("", "My String");
                    break;
                case 9:
                    BasicChecks.checkNotNullAndNotEmpty(new int[] {});
                    break;
                case 10:
                    BasicChecks.checkNotNullAndNotEmpty(new int[] {}, "My Array of atomic int");
                    break;
                case 11:
                    BasicChecks.checkNotNullAndNotEmpty(new Integer[] {});
                    break;
                case 12:
                    BasicChecks.checkNotNullAndNotEmpty(new Integer[] {}, "My Array of Integer");
                    break;
                case 13:
                    BasicChecks.checkNotNullAndNotEmpty(new Integer(42), "Unhandled object");
                    break;
                default:
                    fail("Never get here");
                }
                fail("There should be an Exception");
            } catch (CheckFailedException exe) {
                System.out.println(exe.getMessage());
            }
        }
        BasicChecks.checkNotNullAndNotEmpty("Hello World");
        BasicChecks.checkNotNullAndNotEmpty("  ");
        {
            List<Integer> x = new ArrayList<Integer>();
            x.add(12);
            BasicChecks.checkNotNullAndNotEmpty(x, "My List");
        }
        {
            Map<Integer, Integer> x = new HashMap<Integer, Integer>();
            x.put(12, 12);
            BasicChecks.checkNotNullAndNotEmpty(x, "My Map");
        }
        {
            Set<Integer> x = new HashSet<Integer>();
            x.add(12);
            BasicChecks.checkNotNullAndNotEmpty(x, "My Set");
        }
        BasicChecks.checkNotNullAndNotEmpty(new int[] { 12 });
        BasicChecks.checkNotNullAndNotEmpty(new Integer[] { 12 });
    }

    @Test
    public void testCheckNotNullAndNotOnlyWhitespace() {
        for (int i = 0; i <= 1; i++) {
            try {
                switch (i) {
                case 0:
                    BasicChecks.checkNotNullAndNotOnlyWhitespace(null);
                    break;
                case 1:
                    BasicChecks.checkNotNullAndNotOnlyWhitespace("   ");
                    break;
                case 2:
                    BasicChecks.checkNotNullAndNotOnlyWhitespace(null, "x");
                    break;
                case 3:
                    BasicChecks.checkNotNullAndNotOnlyWhitespace("   ", "x");
                    break;
                default:
                    fail("Never get here");
                }
                fail("There should be an Exception");
            } catch (CheckFailedException exe) {
                System.out.println(exe.getMessage());
            }
        }
        BasicChecks.checkNotNullAndNotOnlyWhitespace("ALPHA", "x");
        BasicChecks.checkNotNullAndNotOnlyWhitespace("  ALPHA  ", "x");
    }

    @Test
    public void testCheckNotNullAndInstanceOf() {
        for (int i = 0; i <= 8; i++) {
            try {
                switch (i) {
                case 0:
                    BasicChecks.checkNotNullAndInstanceOf(null, Number.class);
                    break;
                case 1:
                    BasicChecks.checkNotNullAndInstanceOf(null, null);
                    break;
                case 2:
                    BasicChecks.checkNotNullAndInstanceOf("Oh my god", null);
                    break;
                case 3:
                    BasicChecks.checkNotNullAndInstanceOf("Oh my god", null, "My String");
                    break;
                case 4:
                    BasicChecks.checkNotNullAndInstanceOf("Well no", Number.class);
                    break;
                case 5:
                    BasicChecks.checkNotNullAndInstanceOf("Well no", Number.class, "My String");
                    break;                    
                case 6:
                    BasicChecks.checkNotNullAndInstanceOf(null, Number.class, "My Number");
                    break;
                case 7:
                    BasicChecks.checkNotNullAndInstanceOf("Well no", Number.class, "My Number");
                    break;
                case 8:
                    BasicChecks.checkNotNullAndInstanceOf(Integer.valueOf(42), Long.class, "My Number");
                    break;
                default:
                    fail("Never get here");
                }
                fail("There should be an Exception");
            } catch (CheckFailedException exe) {
                System.out.println(exe.getMessage());
            }
        }
        BasicChecks.checkNotNullAndInstanceOf(Integer.valueOf(42), Number.class, "My Number");
        BasicChecks.checkNotNullAndInstanceOf(Integer.valueOf(42), Integer.class, "My Number");
        BasicChecks.checkNotNullAndInstanceOf(Integer.valueOf(42), Object.class, "My Number");
    }

    @Test    
    public void testCheckElementIndex() {
        List<Integer> emptyList = new ArrayList<Integer>();
        List<Integer> twoElementList = new ArrayList<Integer>();
        twoElementList.add(12);
        twoElementList.add(13);
        for (int i = 0; i <= 8; i++) {
            try {
                switch (i) {
                case 0:
                    BasicChecks.checkElementIndex(-1, twoElementList);
                    break;
                case 1:
                    BasicChecks.checkElementIndex(2, twoElementList);
                    break;
                case 2:
                    BasicChecks.checkElementIndex(0, emptyList);
                    break;
                case 3:
                    BasicChecks.checkElementIndex(1, emptyList);
                    break;
                case 4:
                    BasicChecks.checkElementIndex(3, new int[]{1,2,3});
                    break;
                case 5:
                    BasicChecks.checkElementIndex(3, new Integer[]{1,2,3});
                    break;
                case 6:
                    BasicChecks.checkElementIndex(0, new int[]{});
                    break;
                case 7:
                    BasicChecks.checkElementIndex(0, new Integer[]{});
                    break;
                case 8:
                    BasicChecks.checkElementIndex(0, new Long(8778));
                    break;                    
                default:
                    fail("Never get here");
                }
                fail("There should be an Exception");
            } catch (CheckFailedException exe) {
                System.out.println(exe.getMessage());
            }
        }
        BasicChecks.checkElementIndex(1,twoElementList);
        BasicChecks.checkElementIndex(1,new int[]{55,66,77});
    }
    

}