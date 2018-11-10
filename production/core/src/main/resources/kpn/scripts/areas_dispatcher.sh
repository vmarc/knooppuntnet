#!/usr/bin/env bash
nohup /kpn/overpass/bin/dispatcher --areas --db-dir=/kpn/database >> /kpn/logs/areas-dispatcher-stdout.log 2>&1 &

