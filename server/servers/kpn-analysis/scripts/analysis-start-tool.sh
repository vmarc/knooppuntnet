#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=analysis-start-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/analysis-start-tool-log.xml \
  -Dcom.sun.management.jmxremote.port=5222 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx4G \
  -cp /kpn/bin/dist/server-4.0.0-snapshot.2.jar \
  -Dloader.main=kpn.core.tools.analysis.AnalysisStartTool org.springframework.boot.loader.PropertiesLauncher \
  --database kpn \
  >> /kpn/logs/analysis-start-tool-stdout.log 2>&1 &
