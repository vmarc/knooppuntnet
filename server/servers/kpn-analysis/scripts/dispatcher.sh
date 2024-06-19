#!/usr/bin/env bash
nohup $KPN_ROOT/overpass/bin/dispatcher --osm-base --attic --db-dir=$KPN_ROOT/database --space=1000000000000 --time=1000000000 --rate-limit=0 >> $KPN_ROOT/logs/dispatcher-stdout.log 2>&1 &
