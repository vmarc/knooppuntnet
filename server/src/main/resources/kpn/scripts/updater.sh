#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=updater \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/updater-log.xml \
  -Dcom.sun.management.jmxremote.port=5103 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx512M \
  kpn.core.replicate.UpdaterTool \
  --rootDir /kpn >> /kpn/logs/updater-stdout.log 2>&1 &
