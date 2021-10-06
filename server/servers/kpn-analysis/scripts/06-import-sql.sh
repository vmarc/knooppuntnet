# 06 - Import SQL
#
nohup /kpn/scripts/script.sh "06-import-sql" "cd /kpn/openmaptiles;make clean;make;make import-sql" >> /kpn/logs/06-import-sql.log 2>&1 &