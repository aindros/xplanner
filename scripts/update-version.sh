#!/bin/sh

PROPERTIES=src/main/resources/xplanner.properties
VERSION_POM=`grep '<version' 'pom.xml' | head -1 | sed -E 's/.*<.*>(.*)<.*>/\1/g'`
VERSION_PROP=`grep 'xplanner.version' $PROPERTIES | sed 's/.*=//g'`
BUILD_DATE=`date +%d/%m/%Y`
BUILD_REV=$(expr `grep -E '^xplanner.build.revision' $PROPERTIES | sed 's/.*=//g'` + 1)

if [ $# -gt 0 ]; then
	case $1 in
		--version)
			UPDATE_VERSION=1
			;;
		--revision)
			UPDATE_REVISION=1
			;;
		--revision-number)
			shift
			UPDATE_REVISION=1
			BUILD_REV=$1
			;;
	esac
else
	UPDATE_VERSION=1
	UPDATE_REVISION=1
fi

if [ ! -z $UPDATE_VERSION ]; then
	sed -i '' -E "s/(xplanner.version=).*/\1${VERSION_POM}/g" $PROPERTIES
fi

if [ ! -z $UPDATE_REVISION ]; then
	sed -i '' -E "s|(xplanner.build.date=).*|\1${BUILD_DATE}|g" src/main/resources/xplanner.properties
	sed -i '' -E "s|^(xplanner.build.revision=).*|\1${BUILD_REV}|g" src/main/resources/xplanner.properties
fi
