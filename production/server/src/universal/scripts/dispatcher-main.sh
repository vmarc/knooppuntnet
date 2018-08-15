#!/usr/bin/env bash
#
#
#
nohup /kpn/overpass/bin/dispatcher --osm-base --attic --db-dir=/kpn/database >> /kpn/logs/main-dispatcher-stdout.log 2>&1 &
sleep 1
/kpn/overpass/bin/dispatcher --osm-base --space=1000000000000 --time=1000000000 --rate-limit=0
sleep 1


