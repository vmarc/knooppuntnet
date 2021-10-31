#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=poi-tile-analyzer-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/poi-tile-analyzer-tool-log.xml \
  -Xms128M \
  -Xmx2G \
  -Dcom.sun.management.jmxremote.port=5105 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -cp /kpn/bin/server-experimental.jar \
  -Dloader.main=kpn.core.tools.poi.PoiTileAnalyzerTool org.springframework.boot.loader.PropertiesLauncher \
  --poi-database kpn \
  > /kpn/logs/poi-tile-analyzer-tool-stdout.log 2>&1 &
