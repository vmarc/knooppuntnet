#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=replicator \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/replicator-log.xml \
  -Dcom.sun.management.jmxremote.port=5102 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx512M \
  kpn.core.replicate.ReplicatorTool \
  --replicateDir /kpn/replicate >> /kpn/logs/replicator-stdout.log 2>&1 &
