package name.heavycarbon.checks;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Testing "BasicChecks"
 *
 * 2014.01.22 - Created to test the correct behaviour of "Check" methods
 * 2015.08.07 - Namespace changed from "com.example" to "name.heavycarbon.checks"
 * 2024.06.20 - Updated to Java 21 and JUnit 5, and fixed according to IDE
 *              suggestions.
 ******************************************************************************/

class TestCheck {

    @Test
    void cannotHappen1() {
        assertThrows(Error.class, BasicChecks::cannotHappen);
    }

    @Test
    void cannotHappen2() {
        assertThrows(Error.class, () -> BasicChecks.cannotHappen("Random Text..."));
    }

    @Test
    void cannotHappen3() {
        assertThrows(Error.class, () -> BasicChecks.cannotHappen(new IllegalArgumentException()));
    }

    @Test
    void cannotHappen4() {
        assertThrows(Error.class, () -> BasicChecks.cannotHappen("Random Text...", new IllegalArgumentException()));
    }

    @Test
    void cannotHappen5() {
        assertThrows(Error.class, () -> BasicChecks.cannotHappen("Looks {} it {}...", "like", "happened"));
    }

    @Test
    void cannotHappen6() {
        assertThrows(Error.class, () -> BasicChecks.cannotHappen("Looks {} it {}...", new IllegalArgumentException(), "like", "happened"));
    }

    @Test
    void checkFailed1() {
        assertThrows(CheckFailedException.class, BasicChecks::checkFailed);
    }

    @Test
    void checkFailed2() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkFailed("Check fails hard because of reasons"));
    }

    @Test
    void checkFaile3() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkFailed("Looks {} check {}...", "like", "failed"));
    }

    @Test
    void checkNotNull1() {
        BasicChecks.checkNotNull("Not null");
    }

    @Test
    void checkNotNull2() {
        BasicChecks.checkNotNull("Not null", null);
    }

    @Test
    void checkNotNull3() {
        BasicChecks.checkNotNull("Not null", "My Little Non-Null Pony");
    }

    @Test
    void checkNotNull4() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNull(null));
    }

    @Test
    void checkNotNull5() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNull(null, null));
    }

    @Test
    void checkNotNull6() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNull(null, "My Little Null Pony"));
    }

    @Test
    void checkNotNullAndNotEmpty1() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(null));
    }

    @Test
    void checkNotNullAndNotEmpty2() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new ArrayList<Integer>()));
    }

    @Test
    void checkNotNullAndNotEmpty3() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new ArrayList<Integer>(), "My List"));
    }

    @Test
    void checkNotNullAndNotEmpty4() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new HashMap<Integer, Integer>()));
    }

    @Test
    void checkNotNullAndNotEmpty5() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new HashMap<Integer, Integer>(), "My Map"));
    }

    @Test
    void checkNotNullAndNotEmpty6() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new HashSet<Integer>()));
    }

    @Test
    void checkNotNullAndNotEmpty7() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new HashSet<Integer>(), "My Set"));
    }

    @Test
    void checkNotNullAndNotEmpty8() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(""));
    }

    @Test
    void checkNotNullAndNotEmpty9() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty("", "My String"));
    }

    @Test
    void checkNotNullAndNotEmpty10() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new int[]{}));
    }

    @Test
    void checkNotNullAndNotEmpty11() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new int[]{}, "My Array of atomic int"));
    }

    @Test
    void checkNotNullAndNotEmpty12() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new Integer[]{}));
    }

    @Test
    void checkNotNullAndNotEmpty13() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(new Integer[]{}, "My Array of Integer"));
    }

    @Test
    void checkNotNullAndNotEmpty14() {
        assertThrows(CheckFailedException.class, () -> BasicChecks.checkNotNullAndNotEmpty(42, "Unhandled object"));
    }

    @Test
    void checkNotNullAndNotEmpty15() {
        BasicChecks.checkNotNullAndNotEmpty("Hello World");
    }

    @Test
    void checkNotNullAndNotEmpty16() {
        BasicChecks.checkNotNullAndNotEmpty("  ");
    }

    @Test
    void checkNotNullAndNotEmpty17() {
        List<Integer> x = List.of(12);
        BasicChecks.checkNotNullAndNotEmpty(x, "My List");
    }

    @Test
    void checkNotNullAndNotEmpty18() {
        Map<Integer, Integer> x = new HashMap<>();
        x.put(12, 12);
        BasicChecks.checkNotNullAndNotEmpty(x, "My Map");
    }

    @Test
    void checkNotNullAndNotEmpty19() {
        Set<Integer> x = new HashSet<>();
        x.add(12);
        BasicChecks.checkNotNullAndNotEmpty(x, "My Set");
    }

    @Test
    void checkNotNullAndNotEmpty20() {
        BasicChecks.checkNotNullAndNotEmpty(new int[]{12});
    }

    @Test
    void checkNotNullAndNotEmpty21() {
        BasicChecks.checkNotNullAndNotEmpty(new Integer[]{12});
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace1() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkNotNullAndNotOnlyWhitespace(null));
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace2() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkNotNullAndNotOnlyWhitespace("   "));
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace3() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkNotNullAndNotOnlyWhitespace(null, "x"));
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace4() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkNotNullAndNotOnlyWhitespace("   ", "x"));
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace5() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkNotNullAndNotOnlyWhitespace(null, "bad string 0")
        );
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace6() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkNotNullAndNotOnlyWhitespace("", "bad string 1"));
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace7() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkNotNullAndNotOnlyWhitespace("    ", "bad string 2"));
    }

    @Test
    void checkNotNullAndNotOnlyWhitespace9() {
        BasicChecks.checkNotNullAndNotOnlyWhitespace("ALPHA", "good string");
        BasicChecks.checkNotNullAndNotOnlyWhitespace("  ALPHA  ", "good string");
    }

    @Test
    void checkNotNullAndInstanceOf1() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf(null, Number.class)
        );
    }

    @Test
    void checkNotNullAndInstanceOf2() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf(null, null)
        );

    }

    @Test
    void checkNotNullAndInstanceOf3() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf("Oh my god", null)
        );

    }

    @Test
    void checkNotNullAndInstanceOf4() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf("Oh my god", null, "My String")
        );

    }

    @Test
    void checkNotNullAndInstanceOf5() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf("Well no", Number.class)
        );

    }

    @Test
    void checkNotNullAndInstanceOf6() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf("Well no", Number.class, "My String")
        );

    }

    @Test
    void checkNotNullAndInstanceOf7() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf(null, Number.class, "My Number")
        );
    }

    @Test
    void checkNotNullAndInstanceOf8() {
        assertThrows(CheckFailedException.class, () ->
                BasicChecks.checkNotNullAndInstanceOf("Well no", Number.class, "My Number")
        );
    }

    @Test
    void checkNotNullAndInstanceOf9() {
        BasicChecks.checkNotNullAndInstanceOf(42L, Long.class, "My Number");
        BasicChecks.checkNotNullAndInstanceOf(42L, Number.class, "My Number");
        BasicChecks.checkNotNullAndInstanceOf(42L, Object.class, "My Number");
    }

    @Test
    void checkNotNullAndInstanceOf10() {
        BasicChecks.checkNotNullAndInstanceOf(42, Integer.class, "My Number");
        BasicChecks.checkNotNullAndInstanceOf(42, Number.class, "My Number");
        BasicChecks.checkNotNullAndInstanceOf(42, Object.class, "My Number");
    }

    @Test
    void checkElementIndex1() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(-1, List.of(12, 13))
        );
    }

    @Test
    void checkElementIndex2() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(2, List.of(12, 13))
        );
    }

    @Test
    void checkElementIndex3() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(0, List.of())
        );
    }

    @Test
    void checkElementIndex4() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(1, List.of())
        );
    }

    @Test
    void checkElementIndex5() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(3, new int[]{1, 2, 3})
        );
    }

    @Test
    void checkElementIndex6() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(3, new Integer[]{1, 2, 3})
        );
    }

    @Test
    void checkElementIndex7() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(0, new int[]{})
        );
    }

    @Test
    void checkElementIndex8() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(0, new Integer[]{})
        );
    }

    @Test
    void checkElementIndex9() {
        assertThrows(CheckFailedException.class,
                () -> BasicChecks.checkElementIndex(0, 8778L)
        );
    }

    @Test
    void checkElementIndex10() {
        BasicChecks.checkElementIndex(1, List.of(12, 13));
    }

    @Test
    void checkElementIndex11() {
        BasicChecks.checkElementIndex(1, new int[]{55, 66, 77});
    }
}
