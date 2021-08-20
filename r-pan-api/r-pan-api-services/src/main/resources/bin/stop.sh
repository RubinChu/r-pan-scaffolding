#!/usr/bin/env bash
ps -ef | grep "com.rubin.rpan.RPanApplication" | grep -v grep | awk -F " " '{print $2}' | xargs -r kill
