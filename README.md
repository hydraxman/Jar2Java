# Jar2Java

## Introduction

The output of this project, which is a runnable jar file, is able to transfer jar to organized java files. Futher plan is to analyze them.

## Build

    gradle clean build fatJar

## How to Use
After build, find the output jar in build/libs/, and run it like below:

    java -jar Jar2Java-1.0-SNAPSHOT.jar /Users/bryansharp/jd-common-ide-0.7.1.jar