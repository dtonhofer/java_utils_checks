package com.example.tests;

import org.junit.Test;

import com.example.BasicChecks;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Testing "BasicChecks"
 * 
 * 2014.02.04 - Created to test the correct behaviour of "Check" methods
 ******************************************************************************/

@SuppressWarnings("static-method")
public class JUnit_CheckTrue {
    
    @Test
    public void testCheckTrue_noMessageAndTrue() {
        BasicChecks.checkTrue(true);
    }

    @Test
    public void testCeckTrue_nullMessageAndTrue() {
        BasicChecks.checkTrue(true, null);
    }

    @Test
    public void testCeckTrue_zeroPlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "message");
    }
    
    @Test
    public void testCheckTrue_onePlaceholderAndTrue() {
        BasicChecks.checkTrue(true, "1 placeholder: {}", "A");
    }

    @Test
    public void testCheckTrue_twoPlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "2 placeholders: {} {}", "A", "B");
    }
    
    @Test
    public void testCheckTrue_threePlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "3 placeholders: {} {} {}", "A", "B", "C");
    }
    
    @Test
    public void testCheckTrue_fourPlaceholdersAndTrue() {
        BasicChecks.checkTrue(true, "4 placeholders: {} {} {} {}", "A", "B", "C", "D");
    }
    
}