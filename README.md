# java_utils_checks

## What is this?

Java utility classes to perform basic runtime checks, similar to what is done with Java's "assert" keyword or the [Junit Assert class](http://junit.org/javadoc/latest/org/junit/Assert.html). Also similar to [Google Guava's "Preconditions"](http://code.google.com/p/guava-libraries/wiki/PreconditionsExplained), but with more methods and better text formatting.

For more complete/elegant approaches, see the Wikipedia entry for [Design by Contract](http://en.wikipedia.org/wiki/Design_by_contract). Some projects that seem live in the Design-by_Contract area are:

*For Java:*

* [cofoja Contracts for Java](https://code.google.com/p/cofoja/) - LGPL
* [Java Contract Suite](http://sourceforge.net/projects/jcontracts/) - Apache License V2.0
* [Contract4J](http://www.polyglotprogramming.com/contract4j) - Eclipse Public License V1.0
* [C4J - Design By Contract for Java (2nd gen.)](http://c4j-team.github.io/C4J/theory.html) - Eclipse Public License V1.0

*For Groovy:*

* [GContracts](https://github.com/andresteingress/gcontracts/wiki)

*For an interesting way to express the pass/fail conditions in Java:*

* [Hamcrest Matchers](http://hamcrest.org/JavaHamcrest/)

## Code organization

* Jars ready to use underneath `jars`. The JUnit tests are in a separate jar. So are the sources.
* Source tree underneath `src`, with JUnit tests in the `tests` leaf package.
* A bash script to run the JUnit tests has been provided.
* TODO: Adding a Gradle/MAven build definition.
* The tree under `utils_checks` is actually an Eclipse project, it has a `.classpath` and `.project`file. So everything can be pulled into Eclipse directly via "git repository exploring".

![File Organization](https://github.com/dtonhofer/java_utils_checks/blob/master/FileOrg.png)

## How to use this?

The class `name.heavycarbon.checks.BasicChecks` exports a set of static methods that can be placed into code to perform runtime checks of the presumed program state. Some of these take messages with placeholders and the corresponding arguments as a vararg array. The placeholders in the messages can be [printf style](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html) or [SLF4J style](http://slf4j.org/faq.html#logging_performance) (i.e. the placeholder is indicated by '{}').

`name.heavycarbon.checks.BasicChecks` can be included statically:

    import static name.heavycarbon.checks.BasicChecks.*
 
 Then call the static methods like this:
  
    Long x = callStuff();
    checkNotNull(x):
 
 Some methods return the object that was checked, so you may write:
  
    Long x = checkNotNull(callStuff()):
 
## Exceptions thrown

All of the methods throw `CheckFailedException` derived from [`RuntimeException`](http://docs.oracle.com/javase/7/docs/api/java/lang/RuntimeException.html), instead of the following:

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

Code written if you are feeling lazy and don't care that `assert` should not be used to check parameters. `assert` will throw a a nasty `Error` instead of a `RuntimeException` if one of the checks fail, and may well be switched off at runtime:

    public Map<SamId,SamAddressData> build(List<SamAddress> sal, AdminStructure ads, Integer idx) {
        assert sal!=null && !sal.isEmpty() : "SamAddress list is null or empty";
        assert ads!=null : "AdminStructure is null";
        assert idx!=null && idx > 0 : "Integer index is " + idx;
        assert ads.knowsAbout(idx) : "AdminStructure does not know about index " + idx;
        ...
    
Code written using `Check` methods. You will get catchable `CheckFailedException` and the checks will always be performed:

    public Map<SamId,SamAddressData> build(List<SamAddress> sal, AdminStructure ads, Integer idx) {
        checkNotNullAndNotEmpty(sal,"SamAddress list");
        checkNotNull(ads,"AdminStructure");
        checkNotNullAndLargerThanZero(idx,"Integer index");
        checkTrue(ads.knowsAbout(idx),"AdminStructure does not know about index {}", idx);
        ....

Another example:

    static InputStream getStreamFromFile(File fileName) {
        checkNotNull(fileName, "file name")
        checkTrue(fileName.exists(), "The file '{}' does not exist", fileName)
        checkTrue(fileName.isFile(), "The file '{}' is not a 'normal' file", fileName)
        checkTrue(fileName.canRead(), "The file '{}' is not readable", fileName)
        return new FileInputStream(fileName)
    }

## Ideas for Improvements

* Use [paranamer](https://github.com/paul-hammant/paranamer) to determine parameter names at runtime.

## Further reading

* [argcheck: asserts for C on steroids](http://who-t.blogspot.com/2013/12/argcheck-assert-on-steroids.html)
* [Using Assertions in Java Technology](http://www.oracle.com/us/technologies/java/assertions-139853.html) -- _Qusay H. Mahmoud, Oracle Corp. (2005)_
* [A historical perspective on runtime assertion checking in software development](http://discovery.ucl.ac.uk/4991/1/4991.pdf) -- _Lori A. Clarke, und David S. Rosenblum. ACM SIGSOFT Software Engineering Notes 31(3):25-37 (2006)_
* [The benefits of programming with assertions (a.k.a. assert statements)](http://www.pgbovine.net/programming-with-asserts.htm) -- _Philip J. Guo (2008)_
* [Groovy Power Assert](http://dontmindthelanguage.wordpress.com/2009/12/11/groovy-1-7-power-assert/).
* [Eiffel Documentation: Design by Contract, Assertions and Exceptions](http://docs.eiffel.com/book/method/et-design-contract-tm-assertions-and-exceptions), in particular the [Check instruction] (http://docs.eiffel.com/book/method/et-instructions#Check)
* [Microsoft Code Contracts: Not with a Ten-foot Pole](http://blogs.encodo.ch/news/view_article.php?id=170)

## License

* Released by M-PLIFY S.A. in January 2013 under the [MIT License](http://opensource.org/licenses/MIT). 
* Extensively modified since, all modifications are still under the MIT License.


