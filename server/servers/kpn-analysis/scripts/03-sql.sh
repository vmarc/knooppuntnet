nohup /kpn/scripts/script.sh "03-sql" "cd /kpn/openmaptiles;make clean;make;make import-sql" >> /kpn/logs/03-sql.log 2>&1 &
