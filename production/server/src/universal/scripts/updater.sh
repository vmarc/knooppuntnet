#!/usr/bin/env bash
. /kpn/conf/kpn-env.sh
nohup ${JAVA_HOME}/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/app/conf/log/updater-log.xml \
  -Dcom.sun.management.jmxremote.port=${KPN_PORT_UPDATER} \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.replicate.UpdaterTool \
  --rootDir /kpn >> /kpn/logs/updater-stdout.log 2>&1 &
