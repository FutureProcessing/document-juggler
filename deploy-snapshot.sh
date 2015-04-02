#!/bin/bash

SNAPSHOT=`mvn help:evaluate -Dexpression=project.version | grep -v '\[' | grep SNAPSHOT`;

echo "$TRAVIS_BRANCH"

if [ "$TRAVIS_PULL_REQUEST" == "false" -a "$TRAVIS_BRANCH" == "develop" -a ! -z "$SNAPSHOT" ]; then
    mvn deploy -DskipTests=true --settings ./settings.xml -B
fi
