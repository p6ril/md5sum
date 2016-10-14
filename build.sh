#!/bin/bash

# check the following resources on internet
# http://doc.ubuntu-fr.org/tutoriel/script_shell
# http://www.tldp.org/LDP/abs/html/
# http://tldp.org/LDP/abs/html/string-manipulation.html

checkStatus() {
	if [ $? ]; then
		echo -e " \033[0;32;40mOK\033[0m"
	else
		echo -e " \033[0;31;40mNOK\033[0m"
		exit
	fi
}

PROJECT_NAME=`pwd | egrep -o "[^/]+$"` # assumes that the project's directory name is the same as the project's main class name
PROJECT_PATH=`find . -name "$PROJECT_NAME.java" | sed "s/\.\/src\///" | sed "s/\/$PROJECT_NAME.java//"`
PACKAGE=${PROJECT_PATH//\//\.}

echo
echo "Java file found: src/$PROJECT_PATH/$PROJECT_NAME.java"
echo "Assuming main class package: $PACKAGE.$PROJECT_NAME"
echo

if [ -d build ]; then
	echo -n "Cleaning the build directory ..."
	rm -rf build/*
else
	echo -n "Creating the build directory ..."
	mkdir build
fi
checkStatus

echo -n "Compiling the java files ..."
javac -deprecation -d build src/$PROJECT_PATH/*.java
checkStatus

PROPERTY_FILES_ARRAY=(`find . -name "*.properties"`)
if [ ${#PROPERTY_FILES_ARRAY[@]} -gt 0 ]; then
	echo -n "Copying property files ..."
	for property_file in ${PROPERTY_FILES_ARRAY[@]}; do
		cp $property_file build/
	done
	checkStatus
fi

if [ -d dist ]; then
	if [ -e dist/$PROJECT_NAME.jar ]; then
		echo -n "Deleting any existing jar package ..."
		rm dist/*.jar
	fi
else
	echo -n "Creating the dist directory ..."
	mkdir dist
fi
checkStatus

if [ ! -e dist/manifest.txt ]; then
	echo -n "Creating the manifest file ..."
	echo "Main-Class: $PACKAGE.$PROJECT_NAME
" > ./dist/manifest.txt
	checkStatus
fi

echo -n "Building the jar file ..."
jar cvfm dist/$PROJECT_NAME.jar dist/manifest.txt -C build . > /dev/null
checkStatus
