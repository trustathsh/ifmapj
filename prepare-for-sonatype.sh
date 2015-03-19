#!/bin/bash

PROJECT_NAME=$1
PROJECT_VERSION=$2

if [ $# -ne 2 ] ; then
  echo "Usage: ./prepare-for-sonatype.sh <project-name> <project-version>"
  exit
fi

PROJECT_POM=$PROJECT_NAME-$PROJECT_VERSION.pom
JAVADOC_JAR=$PROJECT_NAME-$PROJECT_VERSION-javadoc.jar
SOURCES_JAR=$PROJECT_NAME-$PROJECT_VERSION-sources.jar
PROJECT_JAR=$PROJECT_NAME-$PROJECT_VERSION.jar

RESULT_DIR=sonatype

mvn clean
mvn javadoc:jar source:jar package

mkdir target/$RESULT_DIR
cp pom.xml target/$RESULT_DIR/$PROJECT_POM
cp target/$JAVADOC_JAR target/$RESULT_DIR
cp target/$SOURCES_JAR target/$RESULT_DIR
cp target/$PROJECT_JAR target/$RESULT_DIR

cd target/$RESULT_DIR
gpg -ab $PROJECT_POM
gpg -ab $JAVADOC_JAR
gpg -ab $SOURCES_JAR
gpg -ab $PROJECT_JAR

jar -cvf bundle.jar $PROJECT_POM $PROJECT_POM.asc $JAVADOC_JAR $JAVADOC_JAR.asc $SOURCES_JAR $SOURCES_JAR.asc $PROJECT_JAR $PROJECT_JAR.asc

