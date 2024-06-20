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

class TestCheckFalse {

    @Test
    void noMessageCheckFalse() {
        BasicChecks.checkFalse(false);
    }

    @Test
    void nullMessageCheckFalse() {
        BasicChecks.checkFalse(false, null);
    }

    @Test
    void zeroPlaceholdersCheckFalse() {
        BasicChecks.checkFalse(false, "message");
    }

    @Test
    void onePlaceholderCheckFalse() {
        BasicChecks.checkFalse(false, "1 placeholder: {}", "A");
    }

    @Test
    void twoPlaceholdersCheckFalse() {
        BasicChecks.checkFalse(false, "2 placeholders: {} {}", "A", "B");
    }

    @Test
    void threePlaceholdersCheckFalse() {
        BasicChecks.checkFalse(false, "3 placeholders: {} {} {}", "A", "B", "C");
    }

    @Test
    void fourPlaceholdersCheckFalse() {
        BasicChecks.checkFalse(false, "4 placeholders: {} {} {} {}", "A", "B", "C", "D");
    }

}
