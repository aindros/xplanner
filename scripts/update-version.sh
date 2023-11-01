#!/bin/sh

PROPERTIES=src/main/resources/xplanner.properties
VERSION_POM=`grep '<version' 'pom.xml' | head -1 | sed -E 's/.*<.*>(.*)<.*>/\1/g'`
VERSION_PROP=`grep 'xplanner.version' $PROPERTIES | sed 's/.*=//g'`
BUILD_DATE=`date +%d/%m/%Y`

if [ "$VERSION_POM" != "$VERSION_PROP" ]; then
    BUILD_REV=0
else
    BUILD_REV=`grep 'xplanner.build.revision' $PROPERTIES | sed 's/.*=//g'`
    BUILD_REV=`expr $BUILD_REV + 1`
fi

sed -i '' -E "s/(xplanner.version=).*/\1${VERSION_POM}/g" $PROPERTIES
sed -i '' -E "s|(xplanner.build.date=).*|\1${BUILD_DATE}|g" src/main/resources/xplanner.properties
sed -i '' -E "s|(xplanner.build.revision=).*|\1${BUILD_REV}|g" src/main/resources/xplanner.properties
