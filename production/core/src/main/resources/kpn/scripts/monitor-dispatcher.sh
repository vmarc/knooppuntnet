#!/usr/bin/env bash
#
# nohup /kpn/scripts/monitor-dispatcher.sh >/kpn/logs/monitor-dispatcher.log &
#
while true
do
	echo ---
	date +"%F %T"
	echo ---
	/kpn/overpass/bin/dispatcher --osm-base --status
	sleep 5
done

