#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=tile-tool \
  -cp /kpn/bin/app/lib/core.core-*.jar:/kpn/bin/app/lib/* \
  -Dconfig.file=/kpn/conf/production.conf \
  -Dlog4j.configurationFile=/kpn/conf/tile-tool-log.xml \
  -Xms256M \
  -Xmx3G \
  -Dcom.sun.management.jmxremote.port=5205 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.TileTool -t /kpn/tiles -a master2 >> /kpn/logs/tile-tool-stdout.log 2>&1 &
