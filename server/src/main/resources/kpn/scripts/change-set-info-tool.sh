#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=change-set-info-tool \
  -Dlog4j.configurationFile=/kpn/conf/change-set-info-tool-log.xml \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Xms128M \
  -Xmx512M \
  -Dcom.sun.management.jmxremote.port=5204 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.ChangeSetInfoTool -c changesets2 -t tasks1 > /kpn/logs/change-set-info-tool-stdout.log 2>&1 &
