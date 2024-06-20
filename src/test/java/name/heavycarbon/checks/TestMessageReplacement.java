package name.heavycarbon.checks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Testing "BasicChecks"
 *
 * 2014.02.04 - Created to test the correct behaviour of "Check" methods
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 * 2024.06.20 - Updated to Java 21 and JUnit 5, and fixed according to IDE
 *              suggestions.
 ******************************************************************************/

class TestMessageReplacement {

    @Test
    void noMessageCheckTrue() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false));
        assertEquals("Test for 'true' fails (no further indication or text)", exe.getMessage());
    }

    @Test
    void noMessageCheckFalse() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkFalse(true));
        assertEquals("Test for 'false' fails (no further indication or text)", exe.getMessage());
    }

    @Test
    void nullMessage() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, null));
        assertNull(exe.getMessage());

    }

    @Test
    void nullMessageAndOneArgument() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, null, "A"));
        assertEquals("The format string was (null). The passed 1 arguments are: 'A'", exe.getMessage());
    }

    @Test
    void nullMessageAndTwoArguments() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, null, "A", "B"));
        assertEquals("The format string was (null). The passed 2 arguments are: 'A' 'B'", exe.getMessage());
    }

    @Test
    void nullMessageAndFiveArguments() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, null, "A", "B", "C", "D", "E"));

        assertEquals("The format string was (null). The passed 5 arguments are: 'A' 'B' 'C' 'D' 'E'", exe.getMessage());

    }

    @Test
    void zeroPlaceholdersZeroArguments() {
        String msg = "message";
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, msg));
        assertEquals(msg, exe.getMessage());
    }

    @Test
    void zeroPlaceholdersZeroArgumentsAndMultilineMessage() {
        String msg = "message line 1\nmessage line 2\n";
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, msg));
        assertEquals(msg, exe.getMessage());
    }

    @Test
    void onePlaceholdersZeroArguments() {
        String msg = "Placeholder {}";
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, msg));
        assertEquals(msg, exe.getMessage());
    }

    @Test
    void onePlaceholderOneArgument() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "one placeholder: {}", Integer.MAX_VALUE));
        assertEquals("one placeholder: 2147483647", exe.getMessage());
    }

    @Test
    void twoPlaceholdersOneArgument() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "two placeholders: {} and {}", Integer.MAX_VALUE));
        assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
    }

    @Test
    void twoPlaceholdersOfDecimalTypeOneArgumnet() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "two placeholders: %d and %2d", Integer.MAX_VALUE));
        assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
    }

    @Test
    void oneBadPlaceholderOneInappropriateArgument() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "one bad placeholders: %d", "WTF"));
        assertTrue(exe.getMessage().startsWith("Exception 'java.util.IllegalFormatConversionException' occurred during formatting of log message"));
    }

    @Test
    void onePlaceholderOneNullArgument() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "one placeholder: {}", null));
        assertEquals("one placeholder: null", exe.getMessage());
    }

    @Test
    void fourPlaceholdersFourArguments() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE));
        assertEquals("four placeholders: 2147483647 and -2147483648 and 1.7976931348623157E308 and 4.9E-324", exe.getMessage());
    }

    @Test
    void fourPlaceholdersAndNoArguments() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}"));
        // TODO: The result looks awkward because it is processed differently...
        assertEquals("four placeholders: {} and {} and {} and {}", exe.getMessage());
    }

    @Test
    void test_fourPlaceholdersAndThreeArguments() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, Double.MAX_VALUE));
        // TODO: This looks bizarre too
        assertEquals("four placeholders: 2147483647 and -2147483648 and 1.7976931348623157E308 and null", exe.getMessage());
    }

    @Test
    void test_fourPlaceholdersTwoArgumentsAndOneNullArray() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, (Object[]) null));
        // TODO: This looks awkward, too
        assertEquals("four placeholders: 2147483647 and -2147483648 and null and null", exe.getMessage());
    }


    @Test
    void test_fourPlaceholdersAndFiveArguments() {
        CheckFailedException exe = assertThrows(CheckFailedException.class, () -> BasicChecks.checkTrue(false, "four placeholders: {} and {} and {} and {}", Integer.MAX_VALUE, Integer.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Long.MAX_VALUE));
        // TODO: Superfluous arguments are dropped on the floor; maybe they should be tacked on
        assertEquals("four placeholders: 2147483647 and -2147483648 and 1.7976931348623157E308 and 4.9E-324", exe.getMessage());
    }

}
