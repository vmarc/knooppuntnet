# 10 - Generate tiles for zoom level 14
#
nohup /kpn/scripts/script.sh "10-generate-tiles-14" "cd /kpn/openmaptiles;export AREA_DC_CONFIG_FILE=/kpn/conf/tiles-config-14.yml;make generate-tiles;mv data/tiles.mbtiles data/tiles.mbtiles.14" >> /kpn/logs/10-generate-tiles-14.log 2>&1 &