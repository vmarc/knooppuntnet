#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=server-history \
  -Dlog4j.configurationFile=/kpn/scripts/conf/server-history-log.xml \
  -Xms512M \
  -Xmx4G \
  -Dcom.sun.management.jmxremote.port=5111 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -jar /kpn/bin/server-history.jar \
  --spring.config.location=classpath:application.properties,file:/kpn/conf/server-history.properties >> /kpn/logs/server-history-stdout.log 2>&1 &
