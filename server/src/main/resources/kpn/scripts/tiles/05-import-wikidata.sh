# 05 - Import Wiki data
#
nohup /kpn/scripts/script.sh "05-import-wikidata" "cd /kpn/openmaptiles;make import-wikidata" >> /kpn/logs/05-import-wikidata.log 2>&1 &