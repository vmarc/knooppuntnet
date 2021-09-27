#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=analyzer-start-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/analyzer-start-tool-log.xml \
  -Dcom.sun.management.jmxremote.port=5222 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx4G \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.analysis.AnalyzerStartTool org.springframework.boot.loader.PropertiesLauncher \
  --analysis attic-analysis --changes attic-changes \
  >> /kpn/logs/analyzer-start-tool-stdout.log 2>&1 &
