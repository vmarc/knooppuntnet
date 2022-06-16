#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=tile-tool-history \
  -Dlog4j.configurationFile=/kpn/scripts/conf/tile-tool-history-log.xml \
  -Dcom.sun.management.jmxremote.port=5207 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx3G \
  -cp /kpn/bin/server-history.jar \
  -Dloader.main=kpn.core.tools.tile.TileTool org.springframework.boot.loader.PropertiesLauncher \
  --tileDir /kpn/tiles-history \
  --host kpn-web \
  --database kpn-prod >> /kpn/logs/tile-tool-history-stdout.log 2>&1 &
