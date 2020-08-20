# Import OpenStreetMap data
#
nohup "$SHELL" << ***eof*** >> /kpn/logs/01-import-osm.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
make import-osm
echo $(date "+%Y-%m-%d %H:%M:%S") Done
echo ---
***eof***