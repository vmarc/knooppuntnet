#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=change-set-info-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/change-set-info-tool-log.xml \
  -Dcom.sun.management.jmxremote.port=5204 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms128M \
  -Xmx512M \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.analysis.ChangeSetInfoTool org.springframework.boot.loader.PropertiesLauncher \
  -c changesets2 -t tasks > /kpn/logs/change-set-info-tool-stdout.log 2>&1 &
