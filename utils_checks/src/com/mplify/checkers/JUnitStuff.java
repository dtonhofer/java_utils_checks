package com.mplify.checkers;

import org.junit.Test;
import static org.junit.Assert.*;

public class JUnitStuff {

    @Test
    public void testIsTrue_noMessage_andTrue() {
        Check.isTrue(true);
    }

    @Test
    public void testIsTrue_noMessage() {
        try {
            Check.isTrue(false);
        } catch (CheckFailedException exe) {
            assertEquals("Test for 'true' fails (no further indication or text)", exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_zeroParameterMessage_andTrue() {
        Check.isTrue(true, "message");
    }

    @Test
    public void testIsTrue_zeroParameterMessage() {
        String msg = "message";
        try {
            Check.isTrue(false, msg);
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_zeroParameterMessageThatIsMultiline() {
        String msg = "message line 1\nmessage line 2\n";
        try {
            Check.isTrue(false, msg);
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_zeroParameterMessageThatIsNull() {
        try {
            Check.isTrue(false, null);
        } catch (CheckFailedException exe) {
            assertEquals(null, exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_zeroParameterMessageWithOnePlaceholder() {
        String msg = "Placeholder {}";
        try {
            Check.isTrue(false, msg);
        } catch (CheckFailedException exe) {
            assertEquals(msg, exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_oneParameterMessage_andTrue() {
        Check.isTrue(true, "one placeholder: {}", Integer.MAX_VALUE);
    }

    @Test
    public void testIsTrue_oneParameterMessage() {
        try {
            Check.isTrue(false, "one placeholder: {}", Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("one placeholder: 2147483647", exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_oneParameterMessageThatIsNull() {
        try {
            Check.isTrue(false, null, Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("The format string was (null). The passed 1 arguments are: '2147483647'", exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_oneParameterMessageWithTwoPlaceholders() {
        try {
            Check.isTrue(false, "two placeholders: {} and {}", Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_oneParameterMessageWithTwoPlaceholdersOfDecimalType() {
        try {
            Check.isTrue(false, "two placeholders: %d and %2d", Integer.MAX_VALUE);
        } catch (CheckFailedException exe) {
            assertEquals("two placeholders: 2147483647 and null", exe.getMessage());
        }
    }

    @Test
    public void testIsTrue_oneParameterMessageWithPlaceholderOfBadType() {
        try {
            Check.isTrue(false, "one bad placeholders: %d", "WTF");
        } catch (CheckFailedException exe) {
            assertTrue(exe.getMessage().startsWith("Exception 'java.util.IllegalFormatConversionException' occurred during formatting of log message"));
        }
    }

    @Test
    public void testIsTrue_oneParameterMessageWithNullParameter() {
        try {
            Check.isTrue(false, "one placeholder: {}", null);
        } catch (CheckFailedException exe) {
            assertEquals("one placeholder: null", exe.getMessage());
        }
    }

}
