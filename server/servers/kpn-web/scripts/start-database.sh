#!/usr/bin/env bash
nohup mongod --config /kpn/conf/mongod.conf >> /kpn/database/logs/mongod-stdout.log 2>&1 &
