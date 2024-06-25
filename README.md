# java_utils_checks

## What is this?

Java utility classes to perform basic runtime checks, similar to what is done with [Java `assert` keyword](https://docs.oracle.com/javase/8/docs/technotes/guides/language/assert.html).

### Can I use something else?

See further below for alternative approaches and libraries. Also:

* There has been a simple object-not-null-checking method call, throwing `NullPointerException`, since Java 7 (July 2011): 
  [`Objects.requireNotNull`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html#requireNonNull(T))
  in [`java.util.Objects`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html)  
* It is recommended to use `@NotNull` and `@Nullable` annotations instead of checking for not-nullness through method calls at runtime (where applicable) because this
  gives an IDE additional information for linting at development time. Very powerful.
   * [Project Lombok](https://objectcomputing.com/resources/publications/sett/january-2010-reducing-boilerplate-code-with-project-lombok) provides
     the `@NotNull` annotation, among other delicious features. The IDE needs a plugin to handle the bytecode rejiggling.
   * If you are using IntellijIDEA, there is `org.jetbrains.annotations.NotNull` and `org.jetbrains.annotations.Nullable`, just include [the project](https://mvnrepository.com/artifact/org.jetbrains/annotations).
   * There are [Jakarta Bean Validation](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/beanvalidation/bean-validation/bean-validation.html) annotations (formerly under `javax.validation`, now under `jakarta.validation`).
     [API doc](https://jakarta.ee/specifications/bean-validation/3.0/apidocs/). 
   * 2011-02: [Which @NotNull Java annotation should I use?](https://stackoverflow.com/questions/4963300/which-notnull-java-annotation-should-i-use)
   * 2023-07: [What @Nullable to use in Java (as of 2023/JDK21)?](https://stackoverflow.com/questions/76630457/what-nullable-to-use-in-java-as-of-2023-jdk21/)
     * which mentions the runtime [Checker framework](https://checkerframework.org/manual/)
  
## How to use this?

The class [`name.heavycarbon.checks.BasicChecks`](src/main/java/name/heavycarbon/checks/BasicChecks.java) exports a set of static methods that can be placed
into code to perform runtime checks of the assumed program state. Some of these take messages with
placeholders and the corresponding arguments as a vararg array. The placeholders in the messages
can be [printf style](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html) or
[SLF4J style](http://slf4j.org/faq.html#logging_performance) (i.e. the placeholder is indicated by '{}').

`name.heavycarbon.checks.BasicChecks` can be included statically:

~~~
import static name.heavycarbon.checks.BasicChecks.*
~~~
 
Then call the static methods like this:

~~~
Long x = callStuff();
checkNotNull(x):
~~~
 
Some methods pass the object that is being check through, so you may write:

~~~
Long x = checkNotNull(callStuff()):
~~~

## Exceptions thrown

All of the methods throw [`CheckFailedException`](src/main/java/name/heavycarbon/checks/CheckFailedException.java) derived 
from [`RuntimeException`](http://docs.oracle.com/javase/7/docs/api/java/lang/RuntimeException.html), instead of the following possibilites:

* [`IllegalArgumentException`](http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html), which seems appropriate for argument checks only ;
* [`IllegalStateException`](http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalStateException.html), for which it is difficult to say when it is appropriate ;
* [`NullPointerException`](http://docs.oracle.com/javase/7/docs/api/java/lang/NullPointerException.html), for which it is difficult to say when it is appropriate and IMHO indicates a bare programming error ;
* [`Error`](http://docs.oracle.com/javase/7/docs/api/java/lang/Error.html), which should terminate the thread and possibly the JVM, (in particular, [`AssertionError`](http://docs.oracle.com/javase/7/docs/api/java/lang/AssertionError.html) falls into this category)
 
Thus:

* You don't need to declare a "throws" clause on the methods all along the call stack ; 
* You can catch and handle the exception if you need to and you are in a place where handling it makes sense ;
* It is clear that it comes from a "Check" method ; 
* It is not considered as unresolvable as an `Error`.

## Example

Problematic code using `assert` to check passed arguments.
`assert` will throw an `Error` instead of a `RuntimeException` if one of the checks fail.
It may well be switched off at runtime, which is not what you want when checking preconditions:

~~~
public Map<SamId,SamAddressData> build(List<SamAddress> sal, AdminStructure ads, Integer idx) {
     assert sal!=null && !sal.isEmpty() : "SamAddress list is null or empty";
     assert ads!=null : "AdminStructure is null";
     assert idx!=null && idx > 0 : "Integer index is " + idx;
     assert ads.knowsAbout(idx) : "AdminStructure does not know about index " + idx;
     ...
~~~
    
Nicer code written using `Check` methods. You will get catchable `CheckFailedException` and the checks will always be performed:

~~~
public Map<SamId,SamAddressData> build(List<SamAddress> sal, AdminStructure ads, Integer idx) {
     checkNotNullAndNotEmpty(sal,"SamAddress list");
     checkNotNull(ads,"AdminStructure");
     checkNotNullAndLargerThanZero(idx,"Integer index");
     checkTrue(ads.knowsAbout(idx),"AdminStructure does not know about index {}", idx);
     ....
~~~

Another example:

~~~
static InputStream getStreamFromFile(File fileName) {
     checkNotNull(fileName, "file name")
     checkTrue(fileName.exists(), "The file '{}' does not exist", fileName)
     checkTrue(fileName.isFile(), "The file '{}' is not a 'normal' file", fileName)
     checkTrue(fileName.canRead(), "The file '{}' is not readable", fileName)
     return new FileInputStream(fileName)
}
~~~

## License

* Released by M-PLIFY S.A. in January 2013 under the [MIT License](http://opensource.org/licenses/MIT). 
* Extensively modified since, all modifications are still under the MIT License.

## Building bytecode and source jars

Use the [Apache Maven](https://maven.apache.org/) tool for this. See the `pom.xml` project declaration for details.

## Ideas for Improvements

Use [paranamer](https://github.com/paul-hammant/paranamer) to determine parameter names at runtime.

# Further reading

## Design by Contract

For more complete/elegant approaches, see the Wikipedia entry for
[Design by Contract](http://en.wikipedia.org/wiki/Design_by_contract) (note that this word construction
has been [trademarked by Eiffel Software](https://en.wikipedia.org/wiki/Design_by_contract#History)).
Some projects that seem live in the Design-by-Contract design space are:

### For Java

* [Cofoja Contracts for Java](https://code.google.com/p/cofoja/), the GitHub project is [here](https://github.com/nhatminhle/cofoja) - LGPL-3.0
* [Java Contract Suite](http://sourceforge.net/projects/jcontracts/) (resurrection of the iContract tool from Reliable Systems) - Apache License V2.0
* [Contract4J](http://deanwampler.github.io/contract4j/) - Eclipse Public License V1.0
* [C4J](https://github.com/C4J-Team/C4J) - Eclipse Public License V1.0

For C4J, see also: [C4J - Design By Contract for Java (2nd gen.)](http://c4j-team.github.io/C4J/index.html)

### For Groovy

* [GContracts](https://github.com/andresteingress/gcontracts/wiki) (archived since 2020-01-07)

# Some References

* 2005: [Using Assertions in Java Technology]([http://www.oracle.com/us/technologies/java/assertions-139853.html](http://web.archive.org/web/20121011111147/http://www.oracle.com/us/technologies/java/assertions-139853.html)) -- _Qusay H. Mahmoud, Oracle Corp._ (via WayBackMachine): "J2SE 1.3 and earlier versions have no built-in support for assertions. They can, however, be provided as an ad hoc solution."
* 2006: [A historical perspective on runtime assertion checking in software development](http://discovery.ucl.ac.uk/4991/1/4991.pdf) -- _Lori A. Clarke, und David S. Rosenblum. ACM SIGSOFT Software Engineering Notes 31(3):25-37 (2006)_
* 2008: [The benefits of programming with assertions (a.k.a. assert statements)](http://www.pgbovine.net/programming-with-asserts.htm) -- _Philip J. Guo_
* 2009: [Groovy Power Assert](http://dontmindthelanguage.wordpress.com/2009/12/11/groovy-1-7-power-assert/)
* 2009: [Microsoft Code Contracts: Not with a Ten-foot Pole](https://www.earthli.com/news/view_article.php?id=2183) -- _marco_ (a lot has changed in the meantime in C#)
* 2013: [argcheck: asserts for C on steroids](http://who-t.blogspot.com/2013/12/argcheck-assert-on-steroids.html) -- _Peter Hutterer_
* 2014: [Programming with Assertions in Java SE 8](https://docs.oracle.com/javase/8/docs/technotes/guides/language/assert.html) -- _Oracle Corp._
* Current Eiffel Documentation: [Design by Contract™, Assertions and Exceptions](https://www.eiffel.org/doc/eiffel/ET-_Design_by_Contract_%28tm%29%2C_Assertions_and_Exceptions), in particular the [Check instruction](https://www.eiffel.org/doc/eiffel/ET-_Instructions#Check)
* 2024: [Assertions in JUnit 4 and JUnit 5](https://www.baeldung.com/junit-assertions) -- _baeldung_

# Addendum: Expressing pass/fail conditions in Java

Easily and succintly expressing pass/fail conditions (assertions, checks) is useful both for _runtime checks_ and _unit tests_.

## At runtime

* [Java `assert` keyword](https://docs.oracle.com/javase/8/docs/technotes/guides/language/assert.html)
* [Google Guava's "Preconditions"](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/base/Preconditions.html)

## In unit tests

Checks come with their respective unit testing frameworks. The most famous being:

* [JUnit 4 `org.junit.Assert` class](https://junit.org/junit4/javadoc/latest/org/junit/Assert.html)
* [JUnit 5 `org.junit.jupiter.api.Assertions` class](https://junit.org/junit5/docs/5.0.1/api/org/junit/jupiter/api/Assertions.html)

Note that a failing JUnit 5 test throws [`org.opentest4j.AssertionFailedError`](https://ota4j-team.github.io/opentest4j/docs/1.3.0/api/org/opentest4j/AssertionFailedError.html)
to ensure compatibility with other unit testing frameworks. That is a subclass of
[`java.lang.AssertionError`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AssertionError.html).
A failing JUnit 4 test throws [`java.lang.AssertionError`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AssertionError.html) directly.

 * [JGoTesting](https://gitlab.com/tastapod/jgotesting)

This is a "modified JUnit" as explained in [this blogpost](https://dannorth.net/scratching-a-junit-itch/).
JGoTesting allows one to perform several checks before a test failure is actually reported, whereas JUnit
breaks off and reports test failure on the first failing check. (Unfortunately `JGoTesting` seems frozen and
is still based on JUnit 4). 

## Mixed

### Hamcrest Matchers

"Hamcrest Matchers" can be used in both in unit tests and at runtime. They often appear in mocking code.

* [Hamcrest Matchers](http://hamcrest.org/JavaHamcrest/) ([API doc](https://hamcrest.org/JavaHamcrest/javadoc/2.2/))

The built-in [assertion](https://hamcrest.org/JavaHamcrest/javadoc/2.2/org/hamcrest/MatcherAssert.html)
throws [`java.lang.AssertionError`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AssertionError.html) on failure.

### AssertJ

"AssertJ" provides fluent (and chainable) assertions for Java, allowing the IDE to suggest method calls as you type.
A failing AssertJ test throws `org.opentest4j.AssertionFailedError`.

* [AssertJ](https://joel-costigliola.github.io/assertj/)

### Google Truth
 
Similar to "AssertJ", "Google Truth" also provides fluent assertions, but does not bother to make then chainable.

* [Truth](https://truth.dev/api/latest/index.html?overview-summary.html)

See also: [Truth vs. AssertJ and Hamcrest](https://truth.dev/comparison.html).

##  Example code

* [Example code for JGoTesting](src/test/java/testing_testing/TestingJGoTesting.java)
* [Example code for Java assertions, Junit 5, Hamcrest Matchers, AssertJ, Google Truth](src/test/java/testing_testing/TestingTesting.java)

For the Maven dependencies, see the [pom.xml](pom.xml).
