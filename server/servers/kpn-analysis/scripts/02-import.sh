nohup /kpn/scripts/script.sh "02-import" "cd /kpn/openmaptiles;make start-db import-data import-osm import-borders import-wikidata" >> /kpn/logs/02-import.log 2>&1 &
