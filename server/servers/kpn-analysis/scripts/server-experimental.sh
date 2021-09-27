#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=server-experimental \
  -Dlog4j.configurationFile=/kpn/scripts/conf/server-experimental-log.xml \
  -Xms512M \
  -Xmx6G \
  -Dcom.sun.management.jmxremote.port=5211 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -jar /kpn/bin/server-experimental.jar \
  --spring.config.location=classpath:application.properties,file:/kpn/conf/server-experimental.properties >> /kpn/logs/server-experimental-stdout.log 2>&1 &
