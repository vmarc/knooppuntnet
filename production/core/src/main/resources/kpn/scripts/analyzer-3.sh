#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/analyzer-3-log.xml \
  -Dcom.sun.management.jmxremote.port=5555 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.AnalyzerTool \
  -i 3 -c changes3 -a master3 -t tasks3 --init-db >> /kpn/logs/analyzer-3-stdout.log 2>&1 &
