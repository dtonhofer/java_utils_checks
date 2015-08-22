package name.heavycarbon.checks.tests;

import org.junit.Test;

import name.heavycarbon.checks.Formatter;

import static org.junit.Assert.*;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * A test case!
 * 
 * 2013.11.18 - Created to test the correct replacement of placeholders
 * 2014.02.01 - Namespace changed from "com.mplify.checkers" to "com.example"
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 ******************************************************************************/

@SuppressWarnings("static-method")
public class JUnit_Formatter {

    @Test
    public void testSlf4jPlaceholderReplacement() {
        assertEquals("",Formatter.replaceSlf4JPlaceholders(""));
        assertEquals("%s",Formatter.replaceSlf4JPlaceholders("{}"));
        assertEquals("%s%s",Formatter.replaceSlf4JPlaceholders("{}{}"));
        assertEquals("xyz",Formatter.replaceSlf4JPlaceholders("xyz"));        
        assertEquals("x%syz",Formatter.replaceSlf4JPlaceholders("x{}yz"));
        assertEquals("xy%sz",Formatter.replaceSlf4JPlaceholders("xy{}z"));
        assertEquals("x%sy%sz",Formatter.replaceSlf4JPlaceholders("x{}y{}z"));
        assertEquals("%sxyz",Formatter.replaceSlf4JPlaceholders("{}xyz"));
        assertEquals("xyz%s",Formatter.replaceSlf4JPlaceholders("xyz{}"));
        assertEquals("%sxyz%s",Formatter.replaceSlf4JPlaceholders("{}xyz{}"));
        assertEquals("%sx%sy%sz%s",Formatter.replaceSlf4JPlaceholders("{}x{}y{}z{}"));
        assertEquals("xyz%s%s",Formatter.replaceSlf4JPlaceholders("xyz{}{}"));
        assertEquals("%s%sxyz",Formatter.replaceSlf4JPlaceholders("{}{}xyz"));
    }
    
    @Test
    public void testSlf4jPlaceholderReplacementWithBackslashEscapes() {
        assertEquals("xy{}z",Formatter.replaceSlf4JPlaceholders("xy\\{}z"));
        assertEquals("xyz{}",Formatter.replaceSlf4JPlaceholders("xyz\\{}"));
        assertEquals("xy\\%sz",Formatter.replaceSlf4JPlaceholders("xy\\\\{}z"));
        assertEquals("xyz\\%s",Formatter.replaceSlf4JPlaceholders("xyz\\\\{}"));        
    }

    @Test
    public void testSlf4jPlaceholderReplacementWithPercentages() {
        assertEquals("xy%%%sz",Formatter.replaceSlf4JPlaceholders("xy%{}z"));
    }
}
