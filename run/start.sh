#!/usr/bin/env bash
# TODO 改成你的项目源码在服务器的全路径
cd '你的项目地址'
mvn clean package -Dmaven.test.skip=true
cd run
rm -rf pan.log pad.txt
nohup ../target/r-pan-1.0.jar > pan.log 2>&1&
echo $! > pid.txt