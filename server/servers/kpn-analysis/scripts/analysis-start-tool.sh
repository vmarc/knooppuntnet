#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=analysis-start-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/analysis-start-tool-log.xml \
  -Dcom.sun.management.jmxremote.port=5222 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx4G \
  -cp /kpn/bin/server-history.jar \
  -Dloader.main=kpn.core.tools.analysis.AnalysisStartTool org.springframework.boot.loader.launch.PropertiesLauncher \
  --database kpn-history \
  >> /kpn/logs/analysis-start-tool-stdout.log 2>&1 &
