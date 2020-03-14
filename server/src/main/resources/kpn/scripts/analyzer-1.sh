#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=analyzer-1 \
  -cp /kpn/bin/app/lib/core.core-*.jar:/kpn/bin/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/analyzer-1-log.xml \
  -Dcom.sun.management.jmxremote.port=5201 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms512M \
  -Xmx2G \
  kpn.core.tools.AnalyzerTool \
  -i 1 -c changes1 -a master2 -t tasks1 --init-db >> /kpn/logs/analyzer-1-stdout.log 2>&1 &
