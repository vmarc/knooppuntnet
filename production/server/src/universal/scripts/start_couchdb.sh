#!/usr/bin/env bash
nohup sudo -i -u couchdb2 couchdb/bin/couchdb > /kpn/logs/couchdb-stdout.log 2>&1 &
