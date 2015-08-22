package name.heavycarbon.checks.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import name.heavycarbon.checks.BasicChecks;
import name.heavycarbon.checks.CheckFailedException;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Testing "BasicChecks"
 * 
 * 2014.02.04 - Created to test the correct behaviour of "Check" methods
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 ******************************************************************************/

@SuppressWarnings("static-method")
public class JUnit_MessageReplacement {

    @Test
    public void test_noMessageCheckTrue() {
        try {
            BasicChecks.checkTrue(false);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("Test for 'true' fails (no further indication or text)", exe.getMessage());
        }
    }

    @Test
    public void test_noMessageCheckFalse() {
        try {
            BasicChecks.checkFalse(true);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("Test for 'false' fails (no further indication or text)", exe.getMessage());
        }
    }

    @Test
    public void test_nullMessage() {
        try {
            BasicChecks.checkTrue(false, null);
        } catch (CheckFailedException exe) {
            assertEquals(null, exe.getMessage());
        }
    }

    @Test
    public void test_nullMessageAndOneArgument() {
        try {
            BasicChecks.checkTrue(false, null, "A");
            fail();
        } catch (CheckFailedException exe) {
            // TODO URG
            assertEquals("The format string was (null). The passed 1 arguments are: 'A'", exe.getMessage());
        }
    }

    @Test
    public void test_nullMessageAndTwoArguments() {
        try {
            BasicChecks.checkTrue(false, null, "A", "B");
            fail();
        } catch (CheckFailedException exe) {
            // TODO URK
            assertEquals("The format string was (null). The passed 2 arguments are: 'A' 'B'", exe.getMessage());
        }
    }

    @Test
    public void test_nullMessageAndFiveArguments() {
        try {
            BasicChecks.checkTrue(false, null, "A", "B", "C", "D", "E");
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("The format string was (null). The passed 5 arguments are: 'A' 'B' 'C' 'D' 'E'", exe.getMessage());
        }
    }
    
    @Test
    public void test_zeroPlaceholdersZeroArguments() {
        String msg = "message";
        try {
            BasicChecks.checkTrue(false, msg);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @Test
    public void test_zeroPlaceholdersZeroArgumentsAndMultilineMessage() {
        String msg = "message line 1\nmessage line 2\n";
        try {
            BasicChecks.checkTrue(false, msg);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @Test
    public void test_onePlaceholdersZeroArguments() {
        String msg = "Placeholder {}";
        try {
            BasicChecks.checkTrue(false, msg);
            fail();            
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void test_onePlaceholderOneArgument() {
        try {
            BasicChecks.checkTrue(false, "one placeholder: {}", Integer.MAX_VALUE);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("one placeholder: 2147483647", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void test_twoPlaceholdersOneArgument() {
        try {
            BasicChecks.checkTrue(false, "two placeholders: {} and {}", Integer.MAX_VALUE);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void test_twoPlaceholdersOfDecimalTypeOneArgumnet() {
        try {
            BasicChecks.checkTrue(false, "two placeholders: %d and %2d", Integer.MAX_VALUE);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
        }
    }

    @Test
    public void test_oneBadPlaceholderOneInappropriateArgument() {
        try {
            BasicChecks.checkTrue(false, "one bad placeholders: %d", "WTF");
            fail();
        } catch (CheckFailedException exe) {
            assertTrue(exe.getMessage().startsWith("Exception 'java.util.IllegalFormatConversionException' occurred during formatting of log message"));
        }
    }

    @Test
    public void test_onePlaceholderOneNullArgument() {
        try {
            BasicChecks.checkTrue(false, "one placeholder: {}", null);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("one placeholder: null", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void test_fourPlaceholdersFourArguments() {
        try {
            BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
            fail();
        } catch (CheckFailedException exe) {
            assertEquals("four placeholders: 2147483647 and -2147483648 and 1.7976931348623157E308 and 4.9E-324", exe.getMessage());
        }
    }

    @Test
    public void test_fourPlaceholersAndNoArguments() {
        try {
            BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}");
            fail();
        } catch (CheckFailedException exe) {
            // TODO: The result looks awkward because it is processed differently...
            assertEquals("four placeholders: {} and {} and {} and {}", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void test_fourPlaceholdersAndThreeArguments() {
        try {
            BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, Double.MAX_VALUE);
            fail();
        } catch (CheckFailedException exe) {
            // TODO: This looks bizarre too
            assertEquals("four placeholders: 2147483647 and -2147483648 and 1.7976931348623157E308 and null", exe.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void test_fourPlaceholdersTwoArgumentsAndOneNullArray() {
        try {
            BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, (Object[]) null);
            fail();
        } catch (CheckFailedException exe) {
            // TODO: This looks awkward, too
            assertEquals("four placeholders: 2147483647 and -2147483648 and null and null", exe.getMessage());
        }
    }
    
    @SuppressWarnings("boxing")
    @Test
    public void test_fourPlaceholdersAndFiveArguments() {
        try {
            BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Long.MAX_VALUE);
            fail();
        } catch (CheckFailedException exe) {
            // TODO: Superfluous arguments are dropped on the floor; maybe they should be tacked on
            assertEquals("four placeholders: 2147483647 and -2147483648 and 1.7976931348623157E308 and 4.9E-324", exe.getMessage());
        }
    }

}
