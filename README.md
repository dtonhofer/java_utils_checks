# java_utils_checks

## What

_Java utility classes to perform simple runtime checks, similarly to what is done with Java's "assert" keyword._

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

For Groovy in particular, see also the [Groovy Power Assert](http://dontmindthelanguage.wordpress.com/2009/12/11/groovy-1-7-power-assert/).


## Further reading

* [argcheck: asserts for C on steroids](http://who-t.blogspot.com/2013/12/argcheck-assert-on-steroids.html)
* [Using Assertions in Java Technology](http://www.oracle.com/us/technologies/java/assertions-139853.html) -- _Qusay H. Mahmoud, Oracle Corp. (2005)_
* [A historical perspective on runtime assertion checking in software development](http://discovery.ucl.ac.uk/4991/1/4991.pdf) -- _Lori A. Clarke, und David S. Rosenblum. ACM SIGSOFT Software Engineering Notes 31(3):25-37 (2006)_
* [The benefits of programming with assertions (a.k.a. assert statements)](http://www.pgbovine.net/programming-with-asserts.htm) -- _Philip J. Guo (2008)_

## How

The class `com.mplify.Check` exports several static methods to perform common checks, in particular checks
of arguments passed to methods. Messages formatted in 
[printf style](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html) or [SLF4J style](http://slf4j.org/faq.html#logging_performance)
(i.e. using '{}' as placeholder) can be passed to these methods.

## Exceptions thrown

All of the methods throw `CheckFailedException` derived from `RuntimeException`, instead of `IllegalArgumentException` and `IllegalStateException` or even an `Error`. Thus:

* You don't need to declare a "throws" clause on the methods all along the call stack ; 
* You can catch and handle the exception if you need to and you are in a place where handling it makes sense ;
* It is clear that it comes from a "check" call ; 
* It is not considered as unresolvable as an [`Error`](http://docs.oracle.com/javase/7/docs/api/java/lang/Error.html)

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
        Check.notNullAndNotEmpty(sal,"SamAddress list");
        Check.notNull(ads,"AdminStructure");
        Check.notNullAndLargerThanZero(idx,"Integer index");
        Check.isTrue(ads.knowsAbout(idx),"AdminStructure does not know about index {}", idx);
        ....

## Loading into Eclipse IDE

The directory "utils_checks" in the git repository is an Eclipse project, so everything can be pulled into Eclipse 
directly via "git repository exploring".

## Ideas for Improvements

* Use [paranamer](https://github.com/paul-hammant/paranamer) to determine parameter names at runtime.

## License

Released by M-PLIFY S.A. in January 2013 under the [MIT License](http://opensource.org/licenses/MIT) 

