package testing_testing;

import org.jgotesting.rule.JGoTestRule;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jgotesting.Assert.*; // same methods as org.junit.Assert.*
import static org.jgotesting.Check.*; // ditto, with different names

// ---
// JGoTesting is based on Junit 4
// https://gitlab.com/tastapod/jgotesting
// https://dannorth.net/scratching-a-junit-itch/
// ---

public class TestingJGoTesting {

    // Change these to elicit test failures.

    private String actualC = "120° Celsius";
    private String expectedC = "120° Celsius";

    private String actualF = "120° Fahrenheit";
    private String expectedF = "120° Fahrenheit";

    // ---
    // Make JUnit4 use JGoTesting
    // ---

    @org.junit.Rule
    public final JGoTestRule test = new JGoTestRule();

    // ---
    // Perform all checks/assertions and report all
    // that fail instead of stopping at the first failure.
    // ---

    @Test
    public void checkSeveralThingsAndReportAllFailures1() {
        // using assert methods
        assertEquals("Celsius", expectedC, actualC);
        assertEquals("Fahrenheit", expectedF, actualF);
        // same again using JGoTesting aliases
        checkEquals("Celsius", expectedC, actualC);
        checkEquals("Fahrenheit", expectedF, actualF);
    }

    @Test
    public void checkSeveralThingsAndReportAllFailures2() {
        test.log("This message only appears of the values differ!");
        // All these are checked, then they all report as failures
        test.check("comparing Celsius with Hamcrest", actualC, equalTo(expectedC))
                .check("comparing Fahrenheit with Hamcrest", actualF, equalTo(expectedF))
                .check("comparing Celsius with equals()", actualC.equals(expectedC))
                .check("comparing Fahrenheit with equals()", actualF.equals(expectedF));
    }

    @Test
    public void checkAndThrowOnFailure() throws Exception {
        test.terminateIf("Most unlikely!", true, is(false));
    }

    // ---
    // We can explicitly fail, but what is missing is a method that throws only if any
    // of the checks done up to that point failed!
    // ---

    @Test
    public void terminateUnconditionally() throws Exception {
        if (!actualC.equals(expectedC)) {
            test.terminate("Some problems turned up already!");
        }
    }
}
