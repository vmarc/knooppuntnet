#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=poi-analyzer-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/poi-analyzer-tool-log.xml \
  -Xms128M \
  -Xmx4G \
  -Dcom.sun.management.jmxremote.port=5105 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -cp /kpn/bin/dist/server-4.0.0-snapshot.17.jar \
  -Dloader.main=kpn.core.tools.poi.PoiAnalyzerTool org.springframework.boot.loader.PropertiesLauncher \
  --poi-database kpn-history \
  > /kpn/logs/poi-analyzer-tool-stdout.log 2>&1 &
