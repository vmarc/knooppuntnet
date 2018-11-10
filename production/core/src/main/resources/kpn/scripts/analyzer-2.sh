#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/analyzer-2-log.xml \
  -Dcom.sun.management.jmxremote.port=5554 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.AnalyzerTool \
  -i 2 -c changes2 -a master2 -t tasks2 --init-db >> /kpn/logs/analyzer-2-stdout.log 2>&1 &
