#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=monitor-route-create-xmls \
  -Dlog4j.configurationFile=/kpn/scripts/conf/monitor-route-create-xmls-tool-log.xml \
  -Xms256M \
  -Xmx3G \
  -cp /kpn/bin/dist/server-3.1.1-alpha-4.jar \
  -Dloader.main=kpn.core.tools.monitor.MonitorRouteCreateXmlsTool org.springframework.boot.loader.launch.PropertiesLauncher \
  >> /kpn/logs/monitor-route-create-xmls-tool-stdout.log 2>&1 &
