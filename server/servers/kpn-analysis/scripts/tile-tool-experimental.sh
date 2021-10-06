#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=tile-tool-experimental \
  -Dlog4j.configurationFile=/kpn/scripts/conf/tile-tool-experimental-log.xml \
  -Dcom.sun.management.jmxremote.port=5206 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx3G \
  -cp /kpn/bin/server-experimental.jar \
  -Dloader.main=kpn.core.tools.tile.TileTool org.springframework.boot.loader.PropertiesLauncher \
  -t /kpn/tiles-experimental \
  -h kpn-frontend \
  -a analysis >> /kpn/logs/tile-tool-experimental-stdout.log 2>&1 &
