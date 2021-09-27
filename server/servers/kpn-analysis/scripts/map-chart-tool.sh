#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=map-chart-tool \
  -Dlog4j.configurationFile=/kpn/conf/map-chart-tool-log.xml \
  -Xms128M \
  -Xmx4G \
  -cp /kpn/bin/dist/server-3.0.10-f.jar \
  -Dloader.main=kpn.core.tools.support.MapChartTool org.springframework.boot.loader.PropertiesLauncher \
  > /kpn/logs/map-chart-tool-stdout.log 2>&1 &
