#!/bin/bash

SNAPSHOT=`mvn help:evaluate -Dexpression=project.version | grep -v '\[' | grep SNAPSHOT`;

if [ "$TRAVIS_PULL_REQUEST" == "false" -a "$TRAVIS_BRANCH" == "publish-shapshot" -a ! -z "$SNAPSHOT" ]; then
    mvn deploy -DskipTests=true --settings ./settings.xml
fi
