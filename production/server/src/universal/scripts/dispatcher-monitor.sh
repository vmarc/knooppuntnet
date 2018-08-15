#!/usr/bin/env bash
#
# nohup /kpn/scripts/monitor_dispatcher.sh >/kpn/logs/monitor.log &
#
while true
do
	echo ---
	date +"%F %T"
	echo ---
	/kpn/overpass/bin/dispatcher --osm-base --status
	sleep 5
done

