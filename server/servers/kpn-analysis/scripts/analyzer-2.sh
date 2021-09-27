#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/bin/server.jar \
  -Dlog4j.configurationFile=/kpn/conf/analyzer-2-log.xml \
  -Dcom.sun.management.jmxremote.port=5202 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.AnalyzerTool \
  -i 2 -c changes-tmp -a master2 -t tasks1 --init-db >> /kpn/logs/analyzer-2-stdout.log 2>&1 &
