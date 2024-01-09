#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=monitor \
  -Dlog4j.configurationFile=/kpn/scripts/conf/monitor-log.xml \
  -Dcom.sun.management.jmxremote.port=5505 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.monitor.MonitorApplication org.springframework.boot.loader.launch.PropertiesLauncher \
  --spring.config.location=classpath:application.properties,file:/kpn/conf/monitor.properties >> /kpn/logs/monitor-stdout.log 2>&1 &
