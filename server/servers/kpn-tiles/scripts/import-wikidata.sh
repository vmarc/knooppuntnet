# Import SQL
# estimate time: 5 minutes ??
#
nohup "$SHELL" << ***eof*** >> /kpn/logs/tiles-import-wikidata.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
make import-wikidata
echo $(date "+%Y-%m-%d %H:%M:%S") Done
***eof***