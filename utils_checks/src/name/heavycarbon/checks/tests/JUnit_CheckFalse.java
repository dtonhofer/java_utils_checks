package name.heavycarbon.checks.tests;

import org.junit.Test;

import name.heavycarbon.checks.BasicChecks;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Testing "BasicChecks"
 * 
 * 2014.02.04 - Created to test the correct behaviour of "Check" methods
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 ******************************************************************************/

@SuppressWarnings("static-method")
public class JUnit_CheckFalse {
    
    @Test
    public void testCheckFalse_noMessageAndFalse() {
        BasicChecks.checkFalse(false);
    }

    @Test
    public void testCeckFalse_nullMessageAndFalse() {
        BasicChecks.checkFalse(false, null);
    }

    @Test
    public void testCeckFalse_zeroPlaceholdersAndFalse() {
        BasicChecks.checkFalse(false, "message");
    }
    
    @Test
    public void testCheckFalse_onePlaceholderAndFalse() {
        BasicChecks.checkFalse(false, "1 placeholder: {}", "A");
    }

    @Test
    public void testCheckFalse_twoPlaceholdersAndFalse() {
        BasicChecks.checkFalse(false, "2 placeholders: {} {}", "A", "B");
    }
    
    @Test
    public void testCheckFalse_threePlaceholdersAndFalse() {
        BasicChecks.checkFalse(false, "3 placeholders: {} {} {}", "A", "B", "C");
    }
    
    @Test
    public void testCheckFalse_fourPlaceholdersAndFalse() {
        BasicChecks.checkFalse(false, "4 placeholders: {} {} {} {}", "A", "B", "C", "D");
    }
    
}
