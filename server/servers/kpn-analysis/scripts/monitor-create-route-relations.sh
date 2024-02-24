#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=monitor-create-route-relations \
  -cp /kpn/bin/dist/server-4.3.6.jar \
  -Dlog4j.configurationFile=/kpn/scripts/conf/monitor-create-route-relations-log.xml \
  -Dloader.main=kpn.core.tools.monitor.support.MonitorCreateRelationsTool \
  org.springframework.boot.loader.launch.PropertiesLauncher > /kpn/logs/monitor-create-route-relations-stdout.log 2>&1 &
