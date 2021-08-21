#!/usr/bin/env bash
JAVA_OPTS="-Xms1024m -Xmx1024m"
java ${JAVA_OPTS} -classpath lib/*:. com.rubin.rpan.RPanApplication --spring.config.location=conf/application.properties > /dev/null 2>&1 &