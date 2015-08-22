#!/bin/bash

SRCJAR="checks-src.jar"
SRCTESTJAR="checks-test-src.jar"
CLASSJAR="checks.jar"
CLASSTESTJAR="checks-test.jar"
LOCATION=$(dirname "$0")
EPROOT="$LOCATION/../eclipse_project_root"
JARDEST="$LOCATION"

pushd "$EPROOT/src/"
if [[ $? != 0 ]]; then
   echo "Cannot cd -- exiting" >&2; exit 1
fi

find . ! -path '*/tests/*' -name *.java | xargs jar cf "$JARDEST/$SRCJAR"
find .   -path '*/tests/*' -name *.java | xargs jar cf "$JARDEST/$SRCTESTJAR"

popd

pushd "$EPROOT/bin/"
if [[ $? != 0 ]]; then
   echo "Cannot cd -- exiting" >&2; exit 1
fi

find . ! -path '*/tests/*' -name *.class | xargs jar cf "$JARDEST/$CLASSJAR"
find .   -path '*/tests/*' -name *.class | xargs jar cf "$JARDEST/$CLASSTESTJAR"

popd

for F in "$SRCJAR" "$SRCTESTJAR" "$CLASSJAR" "$CLASSTESTJAR"; do
   echo "------ Contents of $F --------"
   jar tf "$JARDEST/$F"
done




