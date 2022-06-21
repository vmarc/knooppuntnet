#!/usr/bin/env bash
sudo -H -u server nohup /kpn/java/bin/java \
  -Dname=server-old \
  -Dlog4j.configurationFile=/kpn/scripts/conf/server-old-log.xml \
  -Xms512M \
  -Xmx6G \
  -Dcom.sun.management.jmxremote.port=5301 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -jar /kpn/bin/server-old.jar \
  --spring.config.location=classpath:application.properties,file:/kpn/conf/server-old.properties >> /kpn/logs/server-old-stdout.log 2>&1 &
