package name.heavycarbon.checks;

import org.junit.jupiter.api.Test;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Testing "BasicChecks"
 *
 * 2014.02.04 - Created to test the correct behaviour of "Check" methods
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 * 2024.06.20 - Updated to Java 21 and JUnit 5, and fixed according to IDE
 *              suggestions.
 ******************************************************************************/

class TestCheckTrue {

    @Test
    void noMessageAndTrue() {
        BasicChecks.checkTrue(true);
    }

    @Test
    void nullMessageAndTrue() {
        BasicChecks.checkTrue(true, null);
    }

    @Test
    void zeroPlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "message");
    }

    @Test
    void onePlaceholderAndTrue() {
        BasicChecks.checkTrue(true, "1 placeholder: {}", "A");
    }

    @Test
    void twoPlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "2 placeholders: {} {}", "A", "B");
    }

    @Test
    void threePlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "3 placeholders: {} {} {}", "A", "B", "C");
    }

    @Test
    void fourPlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "4 placeholders: {} {} {} {}", "A", "B", "C", "D");
    }

}
