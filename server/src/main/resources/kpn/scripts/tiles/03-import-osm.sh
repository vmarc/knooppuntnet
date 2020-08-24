# Import OpenStreetMap data
#
nohup /kpn/scripts/script.sh "03-import-osm" "cd /kpn/openmaptiles;make import-osm" >> /kpn/logs/03-import-osm.log 2>&1 &