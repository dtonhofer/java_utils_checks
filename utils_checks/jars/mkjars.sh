#!/bin/bash

SRCJAR="checks-src.jar"
SRCTESTJAR="checks-test-src.jar"
CLASSJAR="checks.jar"
CLASSTESTJAR="checks-test.jar"

LOCATION=$(dirname "$0")

pushd "$LOCATION/../src/"
if [[ $? != 0 ]]; then
   echo "Cannot cd -- exiting" >&2; exit 1
fi

find . ! -path '*/tests/*' -name *.java | xargs jar cf ../jars/$SRCJAR 
find .   -path '*/tests/*' -name *.java | xargs jar cf ../jars/$SRCTESTJAR

popd

pushd "$LOCATION/../bin/"
if [[ $? != 0 ]]; then
   echo "Cannot cd -- exiting" >&2; exit 1
fi

find . ! -path '*/tests/*' -name *.class | xargs jar cf ../jars/$CLASSJAR
find .   -path '*/tests/*' -name *.class | xargs jar cf ../jars/$CLASSTESTJAR

popd

for F in "$SRCJAR" "$SRCTESTJAR" "$CLASSJAR" "$CLASSTESTJAR"; do
   echo "------ Contents of $F --------"
   jar tf "$LOCATION/$F"
done




