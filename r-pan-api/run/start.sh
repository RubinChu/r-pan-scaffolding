#!/usr/bin/env bash
echo start package...
# TODO modify the physical address deployed for your server project
cd <your project physical address>
rm -rf run/api.log run/pid.txt
mvn clean package -Dmaven.test.skip=true
echo package success, start to run the project...
cd run/
# TODO modify the physical address deployed for your server project
nohup java -jar <your project physical address>/target/r-pan-api-3.1.jar > api.log 2>&1&
echo $! > pid.txt
echo start success