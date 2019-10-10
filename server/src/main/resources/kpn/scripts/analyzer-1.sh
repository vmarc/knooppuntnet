#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/analyzer-1-log.xml \
  -Dcom.sun.management.jmxremote.port=5553 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.AnalyzerTool \
  -i 1 -c changes1 -a master1 -t tasks1 --init-db >> /kpn/logs/analyzer-1-stdout.log 2>&1 &
