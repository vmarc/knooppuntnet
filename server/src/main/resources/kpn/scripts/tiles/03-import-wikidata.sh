# Import SQL
#
nohup "$SHELL" << ***eof*** >> /kpn/logs/03-import-wikidata.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
make import-wikidata
echo $(date "+%Y-%m-%d %H:%M:%S") Done
echo ---
***eof***