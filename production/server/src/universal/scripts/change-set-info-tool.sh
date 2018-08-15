#!/usr/bin/env bash
. /kpn/conf/kpn-env.sh
nohup ${JAVA_HOME}/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/app/conf/log/change-set-info-tool-log.xml \
  kpn.core.tools.ChangeSetInfoTool > /kpn/logs/change-set-info-tool-stdout.log 2>&1

