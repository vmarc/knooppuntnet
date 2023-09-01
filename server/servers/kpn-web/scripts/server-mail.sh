#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=server-mail \
  -Dlog4j.configurationFile=/kpn/scripts/conf/server-mail-log.xml \
  -Xms512M \
  -Xmx2G \
  -Xss128M \
  -Dcom.sun.management.jmxremote.port=5103 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -jar /kpn/bin/server.jar \
  --spring.config.location=classpath:application.properties,file:/kpn/conf/server-mail.properties >> /kpn/logs/server-mail-stdout.log 2>&1 &
