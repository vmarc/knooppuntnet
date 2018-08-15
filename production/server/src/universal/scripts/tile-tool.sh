#!/usr/bin/env bash
. /kpn/conf/kpn-env.sh
nohup ${JAVA_HOME}/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/app/conf/log/tile-tool-log.xml \
  kpn.core.tools.TileTool -t /kpn/tiles -a master >> /kpn/logs/tile-tool-stdout.log 2>&1 &
