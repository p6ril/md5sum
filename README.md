# MD5Sum

## Introduction

MD5Sum is a simple java program that calculates or validates file hashes. It supports 3 hash types: MD5, SHA1 and SHA256.

## Build Howto 

The package comes with a bash script (works on linux and mac machines) that does it all for you. Just get the sources from github, go to the program directory and run the build script.

I didn't mention it as it's kinda obvious, but I assume you have a java SDK installed on your system: no SDK, no java compiler. The build script uses Oracle's `javac` compiler.

```
$ git clone https://github.com/p6ril/md5sum.git
$ cd md5sum
$ ./build.sh
```

If everything went ok, the build script should have created a ready to use jar package in the `dist` subdirectory. In order to run the program type in the following commands at the prompt:

```
$ cd dist
$ java -jar MD5Sum.jar
```

Without any more parameter the program will display a short help:

```
ERROR: invalid number of arguments on the command line.
Help:
	MD5Sum calculates or validates file(s) hash.
Syntax:
	java MD5Sum <options> <parameters>

Options:
	-SHA-1 uses the SHA-1 algorithm instead of MD5 by default
	-SHA-256 uses the SHA-256 algorithm instead of MD5 by default
	-FR uses the French locale instead of English by default

Parameters:
	<filename> calculates the checksum of the file which name is passed as an argument
	<filename> <md5sum> validates a file against the checksum passed as an argument
```

Pretty straightforward ain't it?

## Note

This program has been designed in a way that it can be easily translated into any language. By default it'll show English messages. French is also included.
