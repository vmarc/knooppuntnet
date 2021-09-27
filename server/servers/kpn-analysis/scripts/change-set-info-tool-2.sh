#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=change-set-info-tool-2 \
  -Dlog4j.configurationFile=/kpn/scripts/conf/change-set-info-tool-log-2.xml \
  -Xms128M \
  -Xmx512M \
  -Dcom.sun.management.jmxremote.port=5244 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.analysis.ChangeSetInfoTool org.springframework.boot.loader.PropertiesLauncher \
  -c changesets2 -t tasks-test > /kpn/logs/change-set-info-tool-stdout-2.log 2>&1 &
