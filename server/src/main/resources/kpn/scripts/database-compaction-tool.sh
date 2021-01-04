#!/usr/bin/env bash
/kpn/java/bin/java \
  -Dname=database-compaction-tool \
  -Dlog4j.configurationFile=/kpn/conf/database-compaction-tool-log.xml \
  -Xms256M \
  -Xmx2G \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.db.DatabaseCompactionTool org.springframework.boot.loader.PropertiesLauncher \
  --host kpn-database --compactions /kpn/conf/compactions-backend.json > /kpn/logs/database-compaction-tool-stdout.log 2>&1 &
