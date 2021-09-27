#!/usr/bin/env bash
nohup /kpn/overpass/bin/dispatcher --osm-base --attic --db-dir=/kpn/database --space=1000000000000 --time=1000000000 --rate-limit=0 >> /kpn/logs/dispatcher-stdout.log 2>&1 &
