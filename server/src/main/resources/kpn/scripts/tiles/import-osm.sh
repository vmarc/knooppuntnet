# Import OSM
# estimate time: more than 5 hours
#
nohup "$SHELL" << ***eof*** >> /kpn/logs/tiles-import-osm.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
make import-osm
echo $(date "+%Y-%m-%d %H:%M:%S") Done
***eof***