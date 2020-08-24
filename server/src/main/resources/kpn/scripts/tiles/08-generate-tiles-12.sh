# 08 - Generate tiles for zoom level 4 to 12
#
nohup /kpn/scripts/script.sh "08-generate-tiles-12" "cd /kpn/openmaptiles;export AREA_DC_CONFIG_FILE=/kpn/conf/tiles-config-12.yml;make generate-tiles;mv data/tiles.mbtiles data/tiles.mbtiles.12" >> /kpn/logs/08-generate-tiles-12.log 2>&1 &