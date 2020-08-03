# Generate tiles for zoom level 13
# 6 hours ?
#
nohup $SHELL << ***eof*** >> /kpn/logs/tiles-generate-tiles-13.log 2>&1 &
echo ---
echo `date "+%Y-%m-%d %H:%M:%S"` Start
cd /kpn/openmaptiles
make generate-tiles
echo `date "+%Y-%m-%d %H:%M:%S"` Done
***eof***