#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/updater-log.xml \
  -Dcom.sun.management.jmxremote.port=5552 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.replicate.UpdaterTool \
  --rootDir /kpn >> /kpn/logs/updater-stdout.log 2>&1 &
