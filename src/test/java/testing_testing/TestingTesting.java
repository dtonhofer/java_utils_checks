package testing_testing;

import org.junit.jupiter.api.Test;

class TestingTesting {

    // Change these to elicit test failures.

    private String actual = "140° Celsius";
    private String expected = "140° Celsius";

    // ---
    // ** Java Assertions **
    // Do not do it like this! Assertions are meant for (switch-offable) runtime checks in non-testing code.
    // This only works if "assertions" have been enabled in the JVM with "-ea" flag.
    // Throws "java.lang.AssertionError" on check failure.
    // Explainer: https://docs.oracle.com/javase/8/docs/technotes/guides/language/assert.html
    // API doc: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AssertionError.html
    // Note that checking that "an exception is thrown" is not possible and makes no sense for this usage intention.
    // Note that it is not prescribed what the message should say (should it say what 'should be the case'
    // or what 'should not be the case'?). As is not actually meant to be read by an end-user, you can be
    // rather flexible. Best is to use an error code.
    // ---

    private static String foo(int x) {
        return "ERR_0003: Bad things happened with " + x + "!";
    }

    @Test
    void checkingWithJavaAssert() {
        assert expected != null : "ERR_0001: Expected is (null)!";
        assert expected.equals(actual) : "ERR_0002: These are not the same!";
        assert expected != null : foo(100);
    }

    // ---
    // ** Standard JUnit 5 **
    // Pull in URL: https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    // Throws "org.opentest4j.AssertionFailedError" on check failure.
    // Note that "expected" is the FIRST argument.
    // https://ota4j-team.github.io/opentest4j/docs/1.3.0/api/org/opentest4j/AssertionFailedError.html
    // API doc top: https://junit.org/junit5/docs/current/api/
    // API doc: https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html
    // ---

    @Test
    void checkingWithJunit() {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkingExceptionsWithJunit() {
        final Throwable t = org.junit.jupiter.api.Assertions.assertThrows(IllegalAccessError.class,
                () -> {
                    throw new IllegalAccessError("Where are my Celsius");
                });
        org.junit.jupiter.api.Assertions.assertTrue(t.getMessage().contains("Celsius"));
    }

    // ---
    // ** Hamcrest matching **
    // Pull in URL: https://mvnrepository.com/artifact/org.hamcrest/hamcrest
    // Throws "java.lang.AssertionError" on check failure.
    // Note that "expected" is the RIGHTMOST argument, inside a "Matcher".
    // API doc top: https://hamcrest.org/JavaHamcrest/javadoc/2.2/
    // API doc: https://hamcrest.org/JavaHamcrest/javadoc/2.2/org/hamcrest/MatcherAssert.html
    // ---

    @Test
    void checkingWithHamcrest() {
        // No reason given, using Matcher: assertThat(java.lang.String reason, boolean assertion)
        org.hamcrest.MatcherAssert.assertThat(actual, org.hamcrest.Matchers.equalTo(expected));
        // Using a reason and a Matcher: assertThat(java.lang.String reason, T actual, Matcher<? super T> matcher)
        org.hamcrest.MatcherAssert.assertThat("Bad value found", actual, org.hamcrest.Matchers.equalTo(expected));
        // Using a reason and a boolean result: assertThat(java.lang.String reason, boolean assertion)
        org.hamcrest.MatcherAssert.assertThat("Bad value found", org.hamcrest.Matchers.equalTo(expected).matches(actual));
        // Complex matching
        org.hamcrest.MatcherAssert.assertThat(actual,
                org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.equalTo(expected),
                        org.hamcrest.Matchers.containsString("Celsius"),
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Fahrenheit"))
                )
        );
    }

    // ---
    // Hamcrest matching, but throw via JUnit.
    // (There isn't any advantage doing this)
    // ---

    @Test
    void checkingWithHamcrestViaJunit() {
        final boolean check = org.hamcrest.Matchers.equalTo(expected).matches(actual);
        org.junit.jupiter.api.Assertions.assertTrue(check);
    }

    // ---
    // ** AssertJ **
    // AssertJ allows "fluent style" where you use ".assertX()" repeatedly.
    // Throws "org.opentest4j.AssertionFailedError"  on check failure.
    // Note that "actual" is the leftmost argument in the fluent chain.
    // Pull in URL: https://mvnrepository.com/artifact/org.assertj/assertj-core
    // Explainer: https://assertj.github.io/doc/
    // API doc top: https://www.javadoc.io/doc/org.assertj/assertj-core/latest/index.html
    // API doc: https://www.javadoc.io/doc/org.assertj/assertj-core/latest/org/assertj/core/api/Assertions.html
    // ---

    @Test
    void checkingWithAssertJ() {
        org.assertj.core.api.Assertions.assertThat(actual).isEqualTo(expected);
        org.assertj.core.api.Assertions.assertThat(actual).isEqualTo(expected).contains("Celsius").doesNotContain("Fahrenheit");
    }

    @Test
    void checkingExceptionsWithAssertJ1() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
                    throw new IllegalAccessError("Where are my Celsius");
                }).isInstanceOf(IllegalAccessError.class)
                .hasMessageContaining("Celsius");
    }

    @Test
    void checkingExceptionsWithAssertJ2() {
        org.assertj.core.api.Assertions.assertThatExceptionOfType(IllegalAccessError.class)
                .isThrownBy(() -> {
                    throw new IllegalAccessError("Where are my Celsius");
                }).withMessageContaining("Celsius");
    }

    // ---
    // ** Google Truth **
    // Throws "com.google.common.truth.AssertionErrorWithFacts" on check failure.
    // Pull in URL: https://mvnrepository.com/artifact/com.google.truth/truth
    // Explainer: https://truth.dev/
    // API doc top: https://truth.dev/api/latest/index.html?overview-summary.html
    // API doc: https://truth.dev/api/latest/com/google/common/truth/Truth.html
    // Uses "Stack Trace Cleaning":
    // https://truth.dev/stack_trace_cleaner
    // You cannot chain the assertions:
    // https://github.com/google/truth/issues/253
    // "In general, we've avoided chaining of assertions for various reasons (decrease
    // in readability of single assertions, trickier failure messages, harder to pin-point
    // lines during failures, etc)."
    // ---

    @Test
    void checkingWithGoogleTruth() {
        com.google.common.truth.Truth.assertThat(actual).isEqualTo(expected);
        com.google.common.truth.Truth.assertWithMessage("Didn't work!").that(actual).isEqualTo(expected);
        com.google.common.truth.Truth.assertThat(actual).isEqualTo(expected);
        com.google.common.truth.Truth.assertThat(actual).contains("Celsius");
        com.google.common.truth.Truth.assertThat(actual).doesNotContain("Fahrenheit");
    }

    @Test
    void checkingExceptionsWithGoogleTruth() {
        // Use JUnit's "assertThrows()", this is the recommended way for Google Truth
        final Throwable t = org.junit.jupiter.api.Assertions.assertThrows(IllegalAccessError.class,
                () -> {
                    throw new IllegalAccessError("Where are my Celsius");
                });
        com.google.common.truth.Truth.assertThat(t.getMessage()).contains("Celsius");
    }

}
