#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=poi-tile-task-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/poi-tile-task-tool-log.xml \
  -Xms128M \
  -Xmx2G \
  -Dcom.sun.management.jmxremote.port=5105 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -cp /kpn/bin/dist/server-4.0.0-snapshot.17.jar \
  -Dloader.main=kpn.core.tools.poi.PoiTileTaskTool org.springframework.boot.loader.PropertiesLauncher \
  --host kpn-web \
  --poi-database kpn-history \
  --task-database kpn-history \
  > /kpn/logs/poi-tile-task-tool-stdout.log 2>&1 &
