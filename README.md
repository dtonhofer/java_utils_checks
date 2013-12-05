java_utils_checks
=================

What
----

Java utility classes to perform simple runtime checks, similarly to what is done with Java's "assert" keyword.

This is practically the same as [Google Guava's "Preconditions"](http://code.google.com/p/guava-libraries/wiki/PreconditionsExplained).

For more complete/elegant approaches, see the Wikipedia entry for [Design by Contract](http://en.wikipedia.org/wiki/Design_by_contract). 

Some projects that seem live in the DbC area are:

For Java:

* [cofoja Contracts for Java](https://code.google.com/p/cofoja/) - LGPL
* [Java Contract Suite](http://sourceforge.net/projects/jcontracts/) - Apache License V2.0
* [Contract4J](http://www.polyglotprogramming.com/contract4j) - Eclipse Public License V1.0
* [C4J - Design By Contract for Java (2nd gen.)](http://c4j-team.github.io/C4J/theory.html) - Eclipse Public License V1.0

For Groovy:

* [GContracts for Groovy](http://gcontracts.org/)

For Groovy in particular, see also the [Groovy Power Assert](spot.com/2009/05/new-power-assertions-in-groovy.html).

See also:

* [argcheck](http://who-t.blogspot.com/2013/12/argcheck-assert-on-steroids.html) - asserts for C on steroids

How
---

The class `com.mplify.Check` exports several static methods to perform common checks, in particular checks
of arguments passed to methods. Messages formatted in 
[printf style](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html) or [SLF4J style](http://slf4j.org/faq.html#logging_performance)
(i.e. using '{}' as placeholder) can be passed to these methods.

Exceptions thrown
-----------------

All of the methods throw `CheckFailedException` derived from `RuntimeException`, instead of `IllegalArgumentException` and `IllegalStateException` or even an `Error`. Thus:

* You don't need to declare a "throws" clause on the methods all along the call stack ; 
* You can catch and handle the exception if you need to and you are in a place where handling it makes sense ;
* It is clear that it comes from a "check" call ; 
* It is not considered as unresolvable as an [`Error`](http://docs.oracle.com/javase/7/docs/api/java/lang/Error.html)

Loading into Eclipse
--------------------

The directory "utils_checks" in the git repository is an Eclipse project, so everything can be pulled into Eclipse 
directly via "git repository exploring".

Improvements
------------

Using [paranamer](https://github.com/paul-hammant/paranamer) to determine parameter names at runtime.

License
-------

Released by M-PLIFY S.A. in January 2013 under the [MIT License](http://opensource.org/licenses/MIT) 


