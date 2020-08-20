# Import borders
#
nohup "$SHELL" << ***eof*** >> /kpn/logs/02-import-borders.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
make import-borders
echo $(date "+%Y-%m-%d %H:%M:%S") Done
echo ---
***eof***