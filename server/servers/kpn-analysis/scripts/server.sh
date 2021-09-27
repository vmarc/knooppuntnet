#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=server \
  -Dlog4j.configurationFile=/kpn/scripts/conf/server-log.xml \
  -Xms512M \
  -Xmx6G \
  -Dcom.sun.management.jmxremote.port=5101 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -jar /kpn/bin/server.jar \
  --spring.config.location=classpath:application.properties,file:/kpn/conf/server.properties >> /kpn/logs/server-stdout.log 2>&1 &
