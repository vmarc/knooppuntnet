#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/replicator-log.xml \
  -Dcom.sun.management.jmxremote.port=5551 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.replicate.ReplicatorTool \
  --replicateDir /kpn/replicate >> /kpn/logs/replicator-stdout.log 2>&1 &
