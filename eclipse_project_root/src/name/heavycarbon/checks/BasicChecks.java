package name.heavycarbon.checks;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * Function for checking arguments of a method (or more generally doing
 * assert-like checks) and throwing accordingly.
 * 
 * This is practically the same as Guava's "Preconditions", see
 * 
 *    http://code.google.com/p/guava-libraries/wiki/PreconditionsExplained
 * 
 * But with methods for additional functionality.
 * 
 * For more complete approaches, see: 
 * 
 *    http://en.wikipedia.org/wiki/Design_by_contract
 *
 *    ...for Java and Groovy DbC facilities.
 *
 * What is being thrown 
 * --------------------
 * 
 * These functions throw "CheckFailedException" derived from RuntimeException
 * instead of "NullPointerException", "IllegalArgumentException" and
 * "IllegalStateException".
 *
 * Why: 
 * ----
 * 
 * "IllegalArgumentException" is meant to be thrown
 * during argument checks ("Thrown to indicate that a method has been passed 
 * an illegal or inappropriate argument."), but this is not consistently used.
 * 
 * "IllegalStateException" is meant to be thrown if the "state is incorrect".
 * ("Signals that a method has been invoked at an illegal or inappropriate 
 *  time. In other words, the Java environment or Java application is not
 *  in an appropriate state for the requested operation."), but this is not 
 * consistently used either.
 *  
 * As one is never sure whether "IllegalArgumentException" or
 * "IllegalStateException" should be thrown, just throw "CheckFailedException".
 * 
 * "NullPointerException" should mean that there is some uncaught problem
 * in resolving a reference, it should really not be thrown by methods that
 * check whether a reference is null.  
 *
 * Basically, I would like to see the three above only thrown from the default
 * Java libraries, and possibly third part libraries not from my own code.
 * 
 * How to import
 * -------------
 * 
 * Import this class statically:
 * 
 *    import static com.example.BasicChecks.*
 * 
 * Then call at will:
 * 
 *    Long res = callStuff();
 *    checkNotNull(x, "Long returned by callStuff()"):
 * 
 * Some methods return the object that was checked, so you may write:
 * 
 *    Long res = checkNotNull(callStuff(), "Long returned by callStuff()"):
 * 
 * I am sure this is more legible though.
 * 
 * --------------
 * 2010.11.25 - Created because bored to add multiliners to methods entries
 * 2011.02.16 - Added _isInstanceOf() taking multiple parameters
 * 2011.05.30 - Added _cannotHappen()
 * 2011.05.31 - Now throw CheckFailedException instead of IllegalArgumentException.
 *              Some rewrites and renames.
 * 2011.06.16 - Added fail()   
 * 2011.07.12 - formatForMe() has been moved to LogFacilities           
 * 2011.10.19 - Moved the package com.mplify.checkers to "01_ignition"
 *              because it's really used "very early".
 * 2011.11.29 - Added validate() to unify validation calls  
 * 2012.01.18 - In validateIt(): made sure that unreachable validate method
 *              does not throw up the stack     
 * 2012.07.17 - Added inRange() for longs
 * 2012.12.28 - Added NEVER_GET_HERE_BUT_NEEDED_FOR_KEEPING_COMPILER_HAPPY
 *              which is used to tell the developer what's up
 * 2013.01.XX - Released by M-PLIFY S.A. under the MIT License              
 * 2013.02.21 - Added "imply()" 
 * 2013.06.21 - Renamed "_check" to "Check" for consistency
 * 2014.01.19 - Added notNullAndLargerThanZero()
 * 2014.01.21 - Reorganized methods, special cases 1..3 args for 'isTrue()'
 *              and 'isFalse()' added
 * 2014.02.01 - Namespace changed from "com.mplify.checkers" to 
 *              "com.example" for some neutrality. Added more
 *              methods; cleaned things up, made more consistent.    
 * 2014.02.02 - Added checkNotNullwm()                        
 * 2014.08.08 - Removed checkNotNullwm(), or rather gave it the same name as
 *              checkNotNull() for user-friendly integration
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 * 
 * TODO: Needs a "less than"
 * TODO: Text formatting is still not nice :-((
 * TODO: Some trivial conditions on Collections (how to disable these if costly?)
 ******************************************************************************/

public class BasicChecks {

    private static final String CANNOT_HAPPEN_MSG = "Can't happen! Time for a code fix!";
    private static final String LTOETZ = "less than or equal to 0";
    private static final String LTZ = "less than 0";

    /**
     * Set "FORMATTER_ALWAYS_ON" to "true" to generate formatted messages
     * independently of whether a given check has failed or not. The formatted
     * message is printed to STDERR. This is for testing the formatting code
     * only! The formatted message will be preceded by "INNOCUOUS_TEXT" to tell
     * the developer that this is not a message about an actually failed check.
     */

    private static final boolean FORMATTER_ALWAYS_ON = false;
    private static final String INNOCUOUS_TEXT = "Test of formatting: ";

    /**
     * This is used when "cannotHappen()" or "fail()" are called. Calling those
     * methods will *always* result in a raised runtime exception, but the
     * compiler may demand that code include a proper "return" after the call to
     * "cannotHappen()" or "fail()".
     * 
     * To keep the Compiler happy, write things like this:
     * 
     * cannotHappen("WTF, dude -- bailing out!") throw new
     * Error(NEVER_GETTING_HERE_BUT_KEEPING_COMPILER_HAPPY);
     */

    public static final String NEVER_GETTING_HERE_BUT_KEEPING_COMPILER_HAPPY = "D'oh!";

    /**
     * As simple check to see whether Java assertions are "on" (return
     * true/false)
     */

    public static boolean isAssertionsOn() {
        boolean assertionsAreOn = false;
        // the next instruction assigns true to assertionsAreOn only if
        // assertions are on!
        assert (assertionsAreOn = true) == true;
        return assertionsAreOn;
    }

    /**
     * Actually check that assertions are on and throw if they are not
     */

    public static void checkAssertionsOn() {
        checkTrue(isAssertionsOn(), "Assertions should be on; enable them using the JVM '-ea' option!");
    }

    /**
     * "cannotHappen()" is used in places that are not supposed to be traversed
     * at all or ever, for example "defaults" of switch statements where all the
     * cases are supposed to be covered. This method actually throws an "Error"
     * instead of an "Exception" as calling this indicates a program error that
     * needs code fixing presto! This will of course kill the thread or the
     * whole process. Some of these methods take a "Throwable" in case one wants
     * to communicate that said Throwable occurred.
     */

    public static void cannotHappen() {
        cannotHappen((String) null, (Throwable) null, (Object[]) null);
    }

    public static void cannotHappen(String txt) {
        cannotHappen(txt, (Throwable) null, (Object[]) null);
    }

    public static void cannotHappen(Throwable cause) {
        cannotHappen(null, cause, (Object[]) null);
    }

    public static void cannotHappen(String txt, Throwable cause) {
        cannotHappen(txt, cause, (Object[]) null);
    }

    public static void cannotHappen(String txt, Object... args) {
        cannotHappen(txt, (Throwable) null, args);
    }

    public static void cannotHappen(String txt, Throwable cause, Object... args) {
        String newTxt = Formatter.formatForMe(txt, args);
        if (cause == null) {
            if (newTxt == null) {
                throw new Error(CANNOT_HAPPEN_MSG);
            } else {
                throw new Error(CANNOT_HAPPEN_MSG + " " + newTxt);
            }
        } else {
            if (newTxt == null) {
                throw new Error(CANNOT_HAPPEN_MSG, cause);
            } else {
                throw new Error(CANNOT_HAPPEN_MSG + " " + newTxt, cause);
            }
        }
    }

    /**
     * Just throw, because a check failed. This is used by all the other methods
     * but can also be invoked directly. Not called "fail" to avoid clashes with
     * Junit's Assert.fail()
     */

    public static void instaFail() {
        instaFail("Failure with no further information");
    }

    public static void instaFail(String txt) {
        instaFail(txt, (Object[]) null);
    }

    public static void instaFail(String txt, Object... args) {
        String newTxt = Formatter.formatForMe(txt, args);
        throw new CheckFailedException(newTxt);
    }

    /**
     * Check for "null" references. The passed Object is returned (as is done in
     * Guava) so one can call the check "inline". This is the same as checkNotNull() but the String is
     * now a message, possibly followed by parameters to be inserted at placeholder locations.
     */

    public static Object checkNotNull(Object x) {
        return checkNotNull(x, null);
    }

    public static Object checkNotNull(Object x, String txt) {
        //
        // Special handling: if "txt" contains no whitespace, assume it is the name of the
        // passed "x" and construct a special error message
        //
        String msg;
        if (x == null) {
            if (txt == null) {
                msg = "The unnamed Object is (null)";
            } else if (txt.indexOf(' ') < 0) {
                msg = "The Object '" + txt + "' is (null)";
            } else {
                msg = txt;
            }
            instaFail(msg);
        }
        return x;
    }

    public static Object checkNotNull(Object x, String txt, Object arg) {
        checkTrue(x != null, txt, arg);
        return x;
    }

    public static Object checkNotNull(Object x, String txt, Object arg1, Object arg2) {
        checkTrue(x != null, txt, arg1, arg2);
        return x;
    }

    public static Object checkNotNull(Object x, String txt, Object arg1, Object arg2, Object... args) {
        checkTrue(x != null, txt, arg1, args, args);
        return x;
    }

    /**
     * Checking for null
     */

    public static void checkNull(Object x, String txt) {
        checkTrue(x == null, txt);
    }

    public static void checkNull(Object x, String txt, Object arg) {
        checkTrue(x == null, txt, arg);
    }

    public static void checkNull(Object x, String txt, Object arg1, Object arg2) {
        checkTrue(x == null, txt, arg1, arg2);
    }

    public static void checkNull(Object x, String txt, Object arg1, Object arg2, Object... args) {
        checkTrue(x == null, txt, arg1, args, args);
    }

    /**
     * Check that the passed Object is not null and "contains elements". The
     * meaning of "contains elements" depends on the actual type of "x".
     * Collection, Map, CharSequence or Array are currently accepted as types for
     * Object, with their respective evident meaning of "empty". An Object of
     * any other type will cause a CheckFailedException to be raised. The passed
     * Object is returned (as is done in Guava) so one can call the check
     * "inline". One may pass an optional "name" to name the Object in the
     * thrown exception.
     */

    public static Object checkNotNullAndNotEmpty(Object x) {
        return checkNotNullAndNotEmpty(x, null);
    }

    @SuppressWarnings("rawtypes")
    public static Object checkNotNullAndNotEmpty(Object x, String name) {
        checkNotNull(x, name);
        assert x != null;
        if (x instanceof Collection) {
            if (((Collection) x).isEmpty()) {
                if (name == null) {
                    instaFail("The unnamed " + x.getClass().getName() + " is empty");
                } else {
                    instaFail("The " + x.getClass().getName() + " '" + name + "' is empty");
                }
                assert false : "Never get here";
            }
        } else if (x instanceof Map) {
            if (((Map) x).isEmpty()) {
                if (name == null) {
                    instaFail("The unnamed " + x.getClass().getName() + " is empty");
                } else {
                    instaFail("The " + x.getClass().getName() + " '" + name + "' is empty");
                }
                assert false : "Never get here";
            }
        } else if (x instanceof CharSequence) {
            if (((CharSequence) x).length() == 0) {
                if (name == null) {
                    instaFail("The unnamed " + x.getClass().getName() + " is empty");
                } else {
                    instaFail("The " + x.getClass().getName() + " '" + name + "' is empty");
                }
                assert false : "Never get here";
            }
        } else if (x.getClass().isArray()) {
            if (Array.getLength(x) == 0) {
                if (name == null) {
                    instaFail("The unnamed array of type " + x.getClass().getName() + " is empty");
                } else {
                    instaFail("The array of type " + x.getClass().getName() + " '" + name + "' is empty");
                }
                assert false : "Never get here";
            }
        } else {
            instaFail("The passed Object is of type " + x.getClass().getName() + ", which cannot be handled!");
            assert false : "Never get here";
        }
        return x;
    }

    /**
     * Check that CharSequence "x" is not null and contains stuff other than
     * whitespace. An empty CharSequence is also considered to be
     * "only whitespace". The passed CharSequence is returned (as is done in
     * Guava) so one can call the check "inline". One may pass an optional
     * "name" to name the Object in the thrown exception.
     */

    public static CharSequence checkNotNullAndNotOnlyWhitespace(CharSequence x) {
        return checkNotNullAndNotOnlyWhitespace(x, null);
    }

    public static CharSequence checkNotNullAndNotOnlyWhitespace(CharSequence x, String name) {
        checkNotNull(x, name);
        assert x != null;
        if (x.length() == 0) {
            if (name == null) {
                instaFail("The unnamed " + x.getClass().getName() + " is empty (considered to be 'only whitespace')");
            } else {
                instaFail("The " + x.getClass().getName() + " '" + name + "' is empty (considered to be 'only whitespace')");
            }
            assert false : "Never get here";
        }
        int len = x.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(x.charAt(i))) {
                return x; // OUTTA HERE; not whitespace only
            }
        }
        if (name == null) {
            instaFail("The unnamed " + x.getClass().getName() + " is not empty but contains only whitespace");
        } else {
            instaFail("The " + x.getClass().getName() + " '" + name + "'  is not empty but contains only whitespace");
        }
        assert false : "Never get here";
        throw new Error(NEVER_GETTING_HERE_BUT_KEEPING_COMPILER_HAPPY);
    }

    /**
     * Check that object "obj" is an instance of class "clazz". Passing "null"
     * as either "obj" or "clazz" will result in a CheckFailedException. The
     * passed Object is returned (as is done in Guava) so one can call the check
     * "inline". One may pass an optional "name" to name the Object in the
     * thrown exception. For extended error messages, a "txt" formatting string
     * and additional args may be passed, any of which can be null.
     */

    public static Object checkNotNullAndInstanceOf(Object x, Class<?> clazz) {
        return checkNotNullAndInstanceOf(x, clazz, null);
    }

    public static Object checkNotNullAndInstanceOf(Object x, Class<?> clazz, String name) {
        checkNotNull(x, name);
        assert x != null;
        if (clazz == null) {
            if (name == null) {
                instaFail("The Class Object against which to compare the unnamed " + x.getClass().getName() + " is (null)");
            } else {
                instaFail("The Class Object against which to compare the " + x.getClass().getName() + " '" + name + "' is (null)");
            }
            assert false : "Never get here";
        }
        assert clazz != null;
        if (!clazz.isAssignableFrom(x.getClass())) {
            String ps = "The Object '" + name + "' is not of class '" + clazz.getName() + "' but of unassignable class '" + x.getClass().getName() + "'";
            instaFail(ps);
            assert false : "Never get here";
        }
        return x;
    }

    /**
     * Given a list, is the "index" in range
     */

    @SuppressWarnings({ "rawtypes", "boxing" })
    public static void checkElementIndex(int index, List list) {
        checkNotNull(list, "list");
        assert list != null;
        String txt = "The index value {} is out of range for a list with element range [0,{}[";
        if (index < 0 || list.size() <= index) {
            instaFail(txt, index, list.size());
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, index, list.size()));
        }
    }

    /**
     * Given an array, is the "index" in range
     */

    @SuppressWarnings("boxing")
    public static void checkElementIndex(int index, Object array) {
        checkNotNull(array, "array");
        assert array != null;
        checkTrue(array.getClass().isArray(), "The passed Object is not an array but a {}", array.getClass().getName());
        String txt = "The index value {} is out of range for an array with element range [0,{}[";
        if (index < 0 || Array.getLength(array) <= index) {
            instaFail(txt, index, Array.getLength(array));
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, index, Array.getLength(array)));
        }
    }

    /**
     * Given a "container" object, does it contain "member". The "container" can
     * be a Collection or Map or an Array. The "member" can be null, in which case the
     * container checked for "null", and depending on the implementation, that may
     * fail abysmally. For Map, we check the key set (not the value set).
     */

    // TODO: Needs TestCase
    @SuppressWarnings("rawtypes")
    public static void checkMemberInContainer(Object member, Object container) {
        checkNotNull(container, "container");
        assert container != null;
        if (container instanceof Collection) {
            if (!((Collection) container).contains(member)) {
                instaFail("The " + container.getClass().getName() + " does not contain the member " + (member == null ? "(null)" : ("of type " + member.getClass().getName())));
                assert false : "Never get here";
            }
        } else if (container instanceof Map) {
            if (!((Map) container).containsKey(member)) {
                instaFail("The " + container.getClass().getName() + " does not contain the key " + (member == null ? "(null)" : ("of type " + member.getClass().getName())));
                assert false : "Never get here";
            }
        } else if (container.getClass().isArray()) {
            int max = Array.getLength(container);
            for (int i = 0; i < max; i++) {
                Object inArray = Array.get(container, i);
                if (member == null) {
                    if (inArray == null) {
                        // yup, found it!
                        return;
                    }
                } else {
                    if (member.equals(inArray)) {
                        // yup, found it!
                        return;
                    }
                }
            }
            instaFail("The array of type " + container.getClass().getName() + " does not contain the key " + (member == null ? "(null)" : ("of type " + member.getClass().getName())));
            assert false : "Never get here";
        } else {
            instaFail("The passed object of type '" + container.getClass().getName() + "' is not handled -- fix code!");
            assert false : "Never get here";
        }
    }

    /**
     * Typical case of checking a boxed number: x!=null && x>0
     */

    // TODO: Needs TestCase
    public static Number checkLargerThanZero(Number x) {
        return checkLargerThanZero(x, null);
    }

    // TODO: Needs TestCase
    public static Number checkLargerThanZero(Number x, String name) {
        checkNotNull(x, name);
        boolean failure;
        if (x instanceof Integer) {
            failure = (((Integer) x).intValue() <= 0);
        } else if (x instanceof Long) {
            failure = (((Long) x).longValue() <= 0l);
        } else if (x instanceof BigDecimal) {
            failure = (((BigDecimal) x).signum() <= 0);
        } else if (x instanceof BigInteger) {
            failure = (((BigInteger) x).signum() <= 0);
        } else if (x instanceof Byte) {
            failure = (((Byte) x).byteValue() <= 0);
        } else if (x instanceof Double) {
            failure = (((Double) x).doubleValue() <= 0.0d);
        } else if (x instanceof Float) {
            failure = (((Float) x).floatValue() <= 0.0f);
        } else if (x instanceof Short) {
            failure = (((Short) x).shortValue() <= 0);
        } else if (x instanceof AtomicInteger) {
            failure = (((AtomicInteger) x).intValue() <= 0);
        } else if (x instanceof AtomicLong) {
            failure = (((AtomicLong) x).longValue() <= 0l);
        } else {
            instaFail("The passed " + x.getClass().getName() + " is not handled -- fix code!");
            assert false : "Never get here";
            throw new Error(NEVER_GETTING_HERE_BUT_KEEPING_COMPILER_HAPPY);
        }
        failComparison(failure, x, name, "less than or equal to 0");
        return x;
    }

    /**
     * Typical case of checking a boxed number: x!=null && x>=0
     */

    // TODO: Needs TestCase
    public static Number checkLargerOrEqualToZero(Number x) {
        return checkLargerOrEqualToZero(x, null);
    }

    // TODO: Needs TestCase
    public static Number checkLargerOrEqualToZero(Number x, String name) {
        checkNotNull(x, name);
        boolean failure;
        if (x instanceof Integer) {
            failure = (((Integer) x).intValue() < 0);
        } else if (x instanceof Long) {
            failure = (((Long) x).longValue() < 0l);
        } else if (x instanceof BigDecimal) {
            failure = (((BigDecimal) x).signum() < 0);
        } else if (x instanceof BigInteger) {
            failure = (((BigInteger) x).signum() < 0);
        } else if (x instanceof Byte) {
            failure = (((Byte) x).byteValue() < 0);
        } else if (x instanceof Double) {
            failure = (((Double) x).doubleValue() < 0.0d);
        } else if (x instanceof Float) {
            failure = (((Float) x).floatValue() < 0.0f);
        } else if (x instanceof Short) {
            failure = (((Short) x).shortValue() < 0);
        } else if (x instanceof AtomicInteger) {
            failure = (((AtomicInteger) x).intValue() < 0);
        } else if (x instanceof AtomicLong) {
            failure = (((AtomicLong) x).longValue() < 0l);
        } else {
            instaFail("The passed " + x.getClass().getName() + " is not handled -- fix code!");
            assert false : "Never get here";
            throw new Error(NEVER_GETTING_HERE_BUT_KEEPING_COMPILER_HAPPY);
        }
        failComparison(failure, x, name, "less than 0");
        return x;
    }

    /**
     * Typical case of checking a boxed number: x==null || x>0
     */

    // TODO: Needs TestCase
    public static Number checkNullOrElseLargerThanZero(Number x) {
        return checkNullOrElseLargerThanZero(x, null);
    }

    // TODO: Needs TestCase
    public static Number checkNullOrElseLargerThanZero(Number x, String name) {
        if (x != null) {
            return checkLargerThanZero(x, name);
        } else {
            return null;
        }
    }

    /**
     * Typical case of checking a boxed number: x==null || x>=0
     */

    // TODO: Needs TestCase
    public static Number checkNullOrElseLargerOrEqualToZero(Number x) {
        return checkNullOrElseLargerOrEqualToZero(x, null);
    }

    // TODO: Needs TestCase
    public static Number checkNullOrElseLargerOrEqualToZero(Number x, String name) {
        if (x != null) {
            return checkLargerOrEqualToZero(x, name);
        } else {
            return null;
        }
    }

    /**
     * Some "sugar" to make life easier and also avoid incontinent boxing
     */

    // TODO: Needs TestCase
    public static int checkLargerThanZero(int x) {
        return checkLargerThanZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static int checkLargerThanZero(int x, String name) {
        if (x <= 0) {
            failComparison(true, x, name, LTOETZ);
        }
        return x;
    }

    // TODO: Needs TestCase
    public static long checkLargerThanZero(long x) {
        return checkLargerThanZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static long checkLargerThanZero(long x, String name) {
        if (x <= 0) {
            failComparison(true, x, name, LTOETZ);
        }
        return x;
    }

    // TODO: Needs TestCase
    public static float checkLargerThanZero(float x) {
        return checkLargerThanZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static float checkLargerThanZero(float x, String name) {
        if (x <= 0) {
            failComparison(true, x, name, LTOETZ);
        }
        return x;
    }

    // TODO: Needs TestCase
    public static double checkLargerThanZero(double x) {
        return checkLargerThanZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static double checkLargerThanZero(double x, String name) {
        if (x <= 0d) {
            failComparison(true, x, name, LTOETZ);
        }
        return x;
    }

    // TODO: Needs TestCase
    public static int checkLargerOrEqualToZero(int x) {
        return checkLargerOrEqualToZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static int checkLargerOrEqualToZero(int x, String name) {
        if (x <= 0) {
            failComparison(true, x, name, LTZ);
        }
        return x;
    }

    // TODO: Needs TestCase
    public static long checkLargerOrEqualToZero(long x) {
        return checkLargerOrEqualToZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static long checkLargerOrEqualToZero(long x, String name) {
        if (x <= 0l) {
            failComparison(true, x, name, LTZ);
        }
        return x;
    }

    // TODO: Needs TestCase
    public static float checkLargerOrEqualToZero(float x) {
        return checkLargerOrEqualToZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static float checkLargerOrEqualToZero(float x, String name) {
        if (x <= 0f) {
            failComparison(true, x, name, LTZ);
        }
        return x;
    }

    // TODO: Needs TestCase
    public static double checkLargerOrEqualToZero(double x) {
        return checkLargerOrEqualToZero(x, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static double checkLargerOrEqualToZero(double x, String name) {
        if (x <= 0d) {
            failComparison(true, x, name, LTZ);
        }
        return x;
    }

    /**
     * Special case of a "double in a given range" -- lowest <= x <= highest One
     * may ask for swapping of lowest and highest as needed (in case on really does not
     * intend to define the empty range)
     */

    // TODO: Needs TestCase
    public static double checkBetween(double lowest, double highest, double x) {
        return checkBetween(lowest, highest, x, false, null);
    }

    // TODO: Needs TestCase
    public static double checkBetween(double lowest, double highest, double x, String name) {
        return checkBetween(lowest, highest, x, false, name);
    }

    // TODO: Needs TestCase
    public static double checkBetween(double lowest, double highest, double x, boolean swapLimitsAsNeeded) {
        return checkBetween(lowest, highest, x, swapLimitsAsNeeded, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static double checkBetween(double lowest, double highest, double x, boolean swapLimitsAsNeeded, String name) {
        double l = lowest;
        double h = highest;
        if (l > h && swapLimitsAsNeeded) {
            // assume honest error
            double tmp = h;
            h = l;
            l = tmp;
        }
        boolean okIf = l <= x && x <= h;
        if (name != null) {
            checkTrue(okIf, "The double value '{}' is not in the range [{},{}]: {}", name, l, h, x);
        } else {
            checkTrue(okIf, "The unnamed double value is not in the range [{},{}]: {}", l, h, x);
        }
        return x;
    }

    /**
     * Special case of a "float in a given range" -- float <= x <= float One
     * may ask for swapping of lowest and highest as needed (in case on really does not
     * intend to define the empty range)
     */

    // TODO: Needs TestCase
    public static double checkBetween(float lowest, float highest, float x) {
        return checkBetween(lowest, highest, x, false, null);
    }

    // TODO: Needs TestCase
    public static double checkBetween(float lowest, float highest, float x, String name) {
        return checkBetween(lowest, highest, x, false, name);
    }

    // TODO: Needs TestCase
    public static double checkBetween(float lowest, float highest, float x, boolean swapLimitsAsNeeded) {
        return checkBetween(lowest, highest, x, swapLimitsAsNeeded, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static double checkBetween(float lowest, float highest, float x, boolean swapLimitsAsNeeded, String name) {
        float l = lowest;
        float h = highest;
        if (l > h && swapLimitsAsNeeded) {
            // assume honest error
            float tmp = h;
            h = l;
            l = tmp;
        }
        boolean okIf = l <= x && x <= h;
        if (name != null) {
            checkTrue(okIf, "The float value '{}' is not in the range [{},{}]: {}", name, l, h, x);
        } else {
            checkTrue(okIf, "The unnamed float value is not in the range [{},{}]: {}", l, h, x);
        }
        return x;
    }

    /**
     * Special case of an "int in a given range" -- lowest <= x <= highest One
     * may ask for swapping of lowest and highest as needed  (in case on really does not
     * intend to define the empty range)
     */

    // TODO: Needs TestCase
    public static int checkBetween(int lowest, int highest, int x) {
        return checkBetween(lowest, highest, x, false, null);
    }

    // TODO: Needs TestCase
    public static int checkBetween(int lowest, int highest, int x, String name) {
        return checkBetween(lowest, highest, x, false, name);
    }

    // TODO: Needs TestCase
    public static int checkBetween(int lowest, int highest, int x, boolean swapLimitsAsNeeded) {
        return checkBetween(lowest, highest, x, swapLimitsAsNeeded, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static int checkBetween(int lowest, int highest, int x, boolean swapLimitsAsNeeded, String name) {
        int l = lowest;
        int h = highest;
        if (l > h && swapLimitsAsNeeded) {
            // assume honest error
            int tmp = h;
            h = l;
            l = tmp;
        }
        boolean okIf = l <= x && x <= h;
        if (name != null) {
            checkTrue(okIf, "The int value '{}' is not in the range [{},{}]: {}", name, l, h, x);
        } else {
            checkTrue(okIf, "The unnamed int value is not in the range [{},{}]: {}", l, h, x);
        }
        return x;
    }

    /**
     * Special case of an "int in a given range" -- lowest <= x < highest One
     * may ask for swapping of lowest and highest as needed   (in case on really does not
     * intend to define the empty range)
     */

    // TODO: Needs TestCase
    public static int checkBetweenExHigh(int lowest, int highest, int x) {
        return checkBetweenExHigh(lowest, highest, x, false, null);
    }

    // TODO: Needs TestCase
    public static int checkBetweenExHigh(int lowest, int highest, int x, String name) {
        return checkBetweenExHigh(lowest, highest, x, false, name);
    }

    // TODO: Needs TestCase
    public static int checkBetweenExHigh(int lowest, int highest, int x, boolean swapLimitsAsNeeded) {
        return checkBetweenExHigh(lowest, highest, x, swapLimitsAsNeeded, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static int checkBetweenExHigh(int lowest, int highest, int x, boolean swapLimitsAsNeeded, String name) {
        int l = lowest;
        int h = highest;
        if (l > h && swapLimitsAsNeeded) {
            // assume honest error
            int tmp = h;
            h = l;
            l = tmp;
        }
        boolean okIf = l <= x && x < h;
        if (name != null) {
            checkTrue(okIf, "The int value '{}' is not in the range [{},{}[: {}", name, l, h, x);
        } else {
            checkTrue(okIf, "The unnamed int value is not in the range [{},{}[: {}", l, h, x);
        }
        return x;
    }

    /**
     * Special case of an "int in a given range" -- lowest < x <= highest One
     * may ask for swapping of lowest and highest as needed (in case on really does not
     * intend to define the empty range)
     */

    // TODO: Needs TestCase
    public static int checkBetweenExLow(int lowest, int highest, int x) {
        return checkBetweenExLow(lowest, highest, x, false, null);
    }

    // TODO: Needs TestCase
    public static int checkBetweenExLow(int lowest, int highest, int x, String name) {
        return checkBetweenExLow(lowest, highest, x, false, name);
    }

    // TODO: Needs TestCase
    public static int checkBetweenExLow(int lowest, int highest, int x, boolean swapLimitsAsNeeded) {
        return checkBetweenExLow(lowest, highest, x, swapLimitsAsNeeded, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static int checkBetweenExLow(int lowest, int highest, int x, boolean swapLimitsAsNeeded, String name) {
        int l = lowest;
        int h = highest;
        if (l > h && swapLimitsAsNeeded) {
            // assume honest error
            int tmp = h;
            h = l;
            l = tmp;
        }
        boolean okIf = l < x && x <= h;
        if (name != null) {
            checkTrue(okIf, "The int value '{}' is not in the range ]{},{}]: {}", name, l, h, x);
        } else {
            checkTrue(okIf, "The unnamed int value is not in the range ]{},{}]: {}", l, h, x);
        }
        return x;
    }

    /**
     * Special case of an "int in a given range" -- lowest < x <= highest One
     * may ask for swapping of lowest and highest as needed (in case on really does not
     * intend to define the empty range)
     */

    // TODO: Needs TestCase
    public static int checkBetweenExBounds(int lowest, int highest, int x) {
        return checkBetweenExBounds(lowest, highest, x, false, null);
    }

    // TODO: Needs TestCase
    public static int checkBetweenExBounds(int lowest, int highest, int x, String name) {
        return checkBetweenExBounds(lowest, highest, x, false, name);
    }

    // TODO: Needs TestCase
    public static int checkBetweenExBounds(int lowest, int highest, int x, boolean swapLimitsAsNeeded) {
        return checkBetweenExBounds(lowest, highest, x, swapLimitsAsNeeded, null);
    }

    // TODO: Needs TestCase
    @SuppressWarnings("boxing")
    public static int checkBetweenExBounds(int lowest, int highest, int x, boolean swapLimitsAsNeeded, String name) {
        int l = lowest;
        int h = highest;
        if (l > h && swapLimitsAsNeeded) {
            // assume honest error
            int tmp = h;
            h = l;
            l = tmp;
        }
        boolean okIf = l < x && x < h;
        if (name != null) {
            checkTrue(okIf, "The int value '{}' is not in the range ]{},{}[: {}", name, l, h, x);
        } else {
            checkTrue(okIf, "The unnamed int value is not in the range ]{},{}[: {}", l, h, x);
        }
        return x;
    }

    /**
     * Very generic: Check whether a condition yields "true"
     */

    public static void checkTrue(boolean x) {
        if (!x) {
            instaFail("Test for 'true' fails (no further indication or text)");
        }
    }

    public static void checkTrue(boolean x, String txt) {
        if (!x) {
            instaFail(txt);
        }
    }

    public static void checkTrue(boolean x, String txt, Object arg) {
        if (!x) {
            instaFail(txt, arg);
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, arg));
        }
    }

    public static void checkTrue(boolean x, String txt, Object arg1, Object arg2) {
        if (!x) {
            instaFail(txt, arg1, arg2);
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, arg1, arg2));
        }
    }

    public static void checkTrue(boolean x, String txt, Object arg1, Object arg2, Object... args) {
        if (!x) {
            instaFail(txt, recopyArray(arg1, arg2, args));
            // will call the fail(String txt, Object... args) method
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, recopyArray(arg1, arg2, args)));
        }
    }

    /**
     * Very generic: Check whether "a implies b"
     */

    public static void checkImplies(boolean a, boolean b) {
        if (a && !b) {
            instaFail("Test for 'implication' fails (no further indication or text)");
        }
    }

    public static void checkImplies(boolean a, boolean b, String txt) {
        if (a && !b) {
            instaFail(txt);
        }
    }

    public static void checkImplies(boolean a, boolean b, String txt, Object... args) {
        if (a && !b) {
            instaFail(txt, args);
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, args));
        }
    }

    /**
     * Very generic: Check whether a condition yields "false"
     */

    public static void checkFalse(boolean x) {
        if (x) {
            instaFail("Test for 'false' fails (no further indication or text)");
        }
    }

    public static void checkFalse(boolean x, String txt) {
        if (x) {
            instaFail(txt);
        }
    }

    public static void checkFalse(boolean x, String txt, Object arg) {
        if (x) {
            instaFail(txt, arg);
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, arg));
        }
    }

    public static void checkFalse(boolean x, String txt, Object arg1, Object arg2) {
        if (x) {
            instaFail(txt, arg1, arg2);
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, arg1, arg2));
        }
    }

    public static void checkFalse(boolean x, String txt, Object arg1, Object arg2, Object... args) {
        if (x) {
            instaFail(txt, recopyArray(arg1, arg2, args));
        }
        if (FORMATTER_ALWAYS_ON) {
            System.err.println(Formatter.formatForMe(INNOCUOUS_TEXT + txt, recopyArray(arg1, arg2, args)));
        }
    }

    /**
     * This call is used when "validate()" is called on structures that have it.
     * The method looks for a parameterless validate() method and invokes it.
     */

    public static Object validateIt(Object obj) {
        return validateIt(obj, false, false);
    }

    /**
     * This call is used when "validate()" is called on structures that have it.
     * The method looks for a parameterless validate() method and invokes it.
     */

    public static Object validateIt(Object obj, boolean dependsOnAssert, boolean yieldsAssertionError) {
        checkNotNull(obj);
        assert obj != null;
        //
        // Return if "depends on assert" and assertions are currently off
        //
        if (dependsOnAssert && !isAssertionsOn()) {
            return obj;
        }
        //
        // Get the method "validate()", return if it does not exist. The return
        // parameter is not interesting
        //
        Method m = null;
        try {
            m = obj.getClass().getMethod("validate", (Class<?>[]) null);
        } catch (NoSuchMethodException exe) {
            // Why the brutal throw, Java? Ok, do nothing and return at once
            return obj;
        }
        assert m != null;
        //
        // Invoke the method "validate()". This may generate AssertionError or a
        // softer Exception
        //
        Object res = null;
        Throwable tlow = null; // may contain more info
        try {
            res = m.invoke(obj);
        } catch (IllegalAccessException exe) {
            // Possible the class is is not public, we are ok with that
            System.err.println("IllegalAccessException while calling validate() of " + obj.getClass().getName());
            return obj;
        } catch (Throwable t) {
            tlow = t;
        }
        //
        // So what's up? If a Throwable was thrown or validation said "FALSE",
        // assume the game's up
        //
        if (tlow != null || Boolean.FALSE.equals(res)) {
            // Should this yield an AssertionError or a more harmless
            // CheckFailedException?
            // The CheckFailedException is preferred, but the caller may change
            // that.
            String msg = "Validation of object of type '" + obj.getClass().getName() + "' failed";
            if (yieldsAssertionError) {
                throw new AssertionError(msg);
            } else {
                if (tlow == null) {
                    throw new CheckFailedException(msg);
                } else {
                    throw new CheckFailedException(msg, tlow);
                }
            }
        }
        return obj;
    }

    /**
     * Helper for implications; unfortunately the consequent cannot be lazily
     * evaluated :-(
     */

    public static boolean imply(boolean antecedent, boolean consequent) {
        return !antecedent || consequent;
    }

    /**
     * Helper
     */

    private static Object[] recopyArray(Object arg1, Object arg2, Object[] args) {
        int newLength = ((args == null) ? 0 : args.length) + 2;
        Object[] newArray = new Object[newLength];
        newArray[0] = arg1;
        newArray[1] = arg2;
        if (args != null) {
            int j = 2;
            for (int i = 0; i < args.length; i++) {
                newArray[j] = args[i];
                j++;
            }
        }
        return newArray;
    }

    /**
     * Helper
     */

    private static void failComparison(boolean failure, Number x, String name, String cmp) {
        if (failure) {
            if (name == null) {
                instaFail("The unnamed '" + x.getClass().getName() + "' is " + cmp + ": " + x);
            } else {
                instaFail("The  '" + x.getClass().getName() + "' + '" + name + "' is  " + cmp + ": " + x);
            }
            assert false : "Never get here";
        }
    }

}
