#!/bin/bash

#
# Both "hamcrest.jar" and "junit.jar" should be on your CLASSPATH.
#
# If not, get them from here:
#
# https://github.com/junit-team/junit/wiki/Download-and-Install
#
# where links are published that point to the sonatype central repository:
#
# http://search.maven.org/#search|gav|1|g:%22junit%22%20AND%20a:%22junit%22
# http://search.maven.org/#search|gav|1|g:%22org.hamcrest%22%20AND%20a:%22hamcrest-core%22
#
# See http://junit.org/apidocs/org/junit/runner/JUnitCore.html
#

LOCATION=$(dirname "$0")
JAR1=$LOCATION/checks.jar
JAR2=$LOCATION/checks-test.jar

JCORE=org.junit.runner.JUnitCore
MYCLASS=name.heavycarbon.checks.tests.JUnit_All

java -cp "$CLASSPATH:$JAR1:$JAR2" "$JCORE" "$MYCLASS"

