package name.heavycarbon.checks;

import java.util.regex.Pattern;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * Formatting of messages on behalf of "Check". The "formatString" can use the
 * SLF4J placeholder "{}" or the print-style placeholders from
 * java.util.Formatting 
 * 
 * See 
 * 
 * http://slf4j.org/faq.html#string_contents 
 * 
 * See 
 * 
 * http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html 
 * 
 * 2013.01.XX - Released by M-PLIFY S.A. under the MIT License 
 * 2013.11.18 - Extended "formatForMeLow" so that it accepts the SLF4J 
 *              placeholder '{}', which is more generic and easier
 *              to use than '%s '%d' etc.
 * 2014.02.01 - Namespace changed from "com.mplify.checkers" to "com.example"
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 ******************************************************************************/

public class Formatter {

    /**
     * Create a "last ditch effort" string to format arguments, after everything
     * else failed. formatForMe() should be called rarely, so this is called
     * rarely, no need to optimize for speed
     */

    private static String buildLastDitchEffortFormatStr(Object... args) {
        assert args != null;
        StringBuilder buf = new StringBuilder("The format string was (null). The passed " + args.length + " arguments are: ");
        boolean addSep = false;
        for (int i = 0; i < args.length; i++) {
            if (addSep) {
                buf.append(" ");
            }
            buf.append("'%s'");
            addSep = true;
        }
        return buf.toString();
    }

    /**
     * If "formatStr" contains the SLF4J placeholder "{}", replace that
     * placeholder with "%s" This is fraught with special cases.... 
     * 
     * <p>
     * \\{} ----> \%s : A SLF4J placeholder with an escaped backslash 
     * <p>
     * \{} ----> {} : An escaped SLF4J placeholder, yields the literal {}
     * <p>
     * %{} ----> %%%s : An SLF4J placeholder with prepended percent, yields the formatting string %s, with escaped % prepended
     * <p>
     * {} ----> %s : An SLF4J placeholder, yields the formatting string %s
     */

    private static final Pattern PATTERN = Pattern.compile("\\{\\}"); // access is threadsafe

    static public String replaceSlf4JPlaceholders(String formatStringIn) {
        assert formatStringIn != null;
        if (formatStringIn.contains("{}")) {
            String[] splits = PATTERN.split(formatStringIn, -1);
            StringBuilder recompose = new StringBuilder();
            for (int i = 0; i < splits.length - 1; i++) {
                String split = splits[i];
                if (split.endsWith("\\\\")) {
                    recompose.append(split, 0, split.length() - 1);
                    recompose.append("%s");
                } else if (!split.isEmpty()) {
                    if (split.endsWith("\\")) {
                        recompose.append(split, 0, split.length() - 1);
                        recompose.append("{}");
                    } else if (split.endsWith("%")) {
                        recompose.append(split);
                        recompose.append("%%s");
                    } else {
                        recompose.append(split);
                        recompose.append("%s");
                    }
                } else {
                    recompose.append(split);
                    recompose.append("%s");
                }
            }
            recompose.append(splits[splits.length - 1]);
            return recompose.toString();
        } else {
            return formatStringIn;
        }
    }

    /**
     * Format using a printf-like formatter. The "formatStrIn" can be null,
     * meaning "use a default".
     */

    private static String formatForMeLow(String formatStr, Object... args) {
        assert args != null;
        //
        // If "formatStr" is null, select a default format string, otherwise
        // replace the SLF4J
        // placeholders '{}' by '%s'
        //
        String formatStrLocal;
        Object[] argsLocal = args;
        if (formatStr == null) {
            // number of args will surely match as the formatStrLocal is BUILT
            // based on the number of args
            formatStrLocal = buildLastDitchEffortFormatStr(args);
        } else {
            // there may actually not be enough args to satisfy the
            // formatter....
            formatStrLocal = replaceSlf4JPlaceholders(formatStr);
            int parameterCount = formatStrLocal.replace("%%", "").split("%").length - 1;
            if (parameterCount > args.length) {
                argsLocal = new Object[parameterCount];
                int i = 0;
                while (i < args.length) {
                    argsLocal[i] = args[i];
                    i++;
                }
                // the remainder of the paramters stays null, which will be
                // printed as "null"
            }
        }
        //
        // Format to "res"; if an Exception occurs, return its description
        //
        StringBuilder res = new StringBuilder();
        try (java.util.Formatter formatter = new java.util.Formatter(res)) {
            formatter.format(formatStrLocal, argsLocal);
            return res.toString();
        } catch (Exception exe) {
            StringBuilder buf = new StringBuilder();
            buf.append("Exception '");
            buf.append(exe.getClass().getName());
            buf.append("' occurred during formatting of log message.\n");
            buf.append("Format string: ");
            buf.append("'");
            buf.append(formatStrLocal.trim());
            buf.append("'\n");
            buf.append("Exception: ");
            buf.append(exe.getClass().getName());
            if (exe.getMessage() != null) {
                buf.append(": ");
                buf.append(exe.getMessage().trim());
            }
            buf.append("'\n");
            for (int i = 0; i < args.length; i++) {
                buf.append("Argument ");
                buf.append(i);
                buf.append(": '");
                buf.append(args[i]);
                buf.append("'\n");
            }
            return buf.toString();
        }
    }

    /**
     * Generate a string given a "formatStr", which contains formatting
     * information according to java.util.Formatter
     * (<a href="https://docs.oracle.com/en/java/javase/21/docs//api/java.base/java/util/Formatter.html">href="https://docs.oracle.com/en/java/javase/21/docs//api/java.base/java/util/Formatter.html</a>) or
     * formatting information according to SLF4J
     * (<a href="http://slf4j.org/faq.html#logging_performance">http://slf4j.org/faq.html#logging_performance</a>)
     */

    public static String formatForMe(String formatStr, Object... args) {
        //
        // In a particularly rare case of a call from Groovy, "args" can be null.
        // This is actually a Groovy bug which should be fixed at some point.
        // However, we interprete a "null" as "do not format"
        //
        if (args == null) {
            return formatStr; // which may be null!!
        }
        //
        // Now format. Problems may occur - in particular, args[] may be too
        // short for the format spec.
        // A (null) args[i] yields the string "null".
        // For objects and the %s format specifier, Java invokes .toString() on
        // the object (which could throw).
        //
        return formatForMeLow(formatStr, args);
    }
}
