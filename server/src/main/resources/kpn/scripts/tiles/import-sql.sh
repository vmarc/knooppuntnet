# Import SQL
# estimate time: 3 hours ??
#
nohup "$SHELL" << ***eof*** >> /kpn/logs/tiles-import-sql.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
make import-sql
echo $(date "+%Y-%m-%d %H:%M:%S") Done
***eof***