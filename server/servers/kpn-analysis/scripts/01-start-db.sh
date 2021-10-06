#
#
nohup /kpn/scripts/script.sh "01-start-db" "cd /kpn/openmaptiles;make start-db" >> /kpn/logs/01-start-db.log 2>&1 &