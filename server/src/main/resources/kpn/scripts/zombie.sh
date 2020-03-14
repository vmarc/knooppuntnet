#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/server-2.0.16-SNAPSHOT-3/lib/core.core-*.jar:/kpn/server-2.0.16-SNAPSHOT-3/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/zombie.log.xml \
  -Dcom.sun.management.jmxremote.port=5206 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.DeactivateZombiesTool \
  127.0.0.1 master1 >> /kpn/logs/zombie-stdout.log 2>&1 &
