#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=database-compaction-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/database-compaction-tool-log.xml \
  -Xms128M \
  -Xmx512M \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.db.DatabaseCompactionTool org.springframework.boot.loader.PropertiesLauncher \
  --host kpn-database --compactions /kpn/conf/compactions.json > /kpn/logs/database-compaction-tool-stdout.log 2>&1 &
