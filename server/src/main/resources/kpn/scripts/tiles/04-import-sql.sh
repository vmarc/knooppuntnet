# Import SQL
#
nohup /bin/bash << ***eof*** >> /kpn/logs/04-import-sql.log 2>&1 &
echo ---
echo $(date "+%Y-%m-%d %H:%M:%S") Start
cd /kpn/openmaptiles
/bin/bash -c "make import-sql"
echo $(date "+%Y-%m-%d %H:%M:%S") Done
***eof***
