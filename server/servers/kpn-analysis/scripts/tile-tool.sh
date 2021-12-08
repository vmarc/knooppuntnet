#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=tile-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/tile-tool-log.xml \
  -Dcom.sun.management.jmxremote.port=5205 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx3G \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.tile.TileTool org.springframework.boot.loader.PropertiesLauncher \
  -t /kpn/tiles \
  -h kpn-tiles \
  -a analysis-experimental >> /kpn/logs/tile-tool-stdout.log 2>&1 &
