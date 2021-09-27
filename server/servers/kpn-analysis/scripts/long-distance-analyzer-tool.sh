#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=long-distance-tool \
  -Dlog4j.configurationFile=/kpn/conf/long-distance-analyzer-tool-log.xml \
  -Dcom.sun.management.jmxremote.port=5205 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx3G \
  -cp /kpn/bin/dist/server-3.1.1-alpha-3.jar \
  -Dloader.main=kpn.core.tools.longdistance.LongDistanceRouteAnalyzerTool org.springframework.boot.loader.PropertiesLauncher \
  >> /kpn/logs/long-distance-analyzer-tool-stdout.log 2>&1 &
