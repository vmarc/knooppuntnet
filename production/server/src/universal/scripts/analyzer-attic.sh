#!/usr/bin/env bash
#
#
#
. /kpn/conf/kpn-env.sh
nohup ${JAVA_HOME}/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/app/conf/log/analyzer-attic-log.xml \
  -Dcom.sun.management.jmxremote.port=${KPN_PORT_ANALYSIS_ATTIC} \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.AnalyzerTool \
  -a ${KPN_DATABASE_ATTIC_ANALYSIS} \
  -c ${KPN_DATABASE_ATTIC_CHANGES} \
  --role attic \
  -t tasks --init-db >> /kpn/logs/analyzer-attic-stdout.log 2>&1 &
