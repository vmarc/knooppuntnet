# Generate DC config
# estimate time: 5 minutes ??
#
nohup $SHELL << ***eof*** >> /kpn/logs/tiles-generate-dc-config.log 2>&1 &
echo ---
echo `date "+%Y-%m-%d %H:%M:%S"` Start
cd /kpn/openmaptiles
make generate-dc-config
echo `date "+%Y-%m-%d %H:%M:%S"` Done
***eof***