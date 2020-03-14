#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dlog4j.configurationFile=/kpn/conf/change-set-info-tool-log.xml \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  kpn.core.tools.analysis.ChangeSetInfoTool > /kpn/logs/change-set-info-tool-stdout.log 2>&1 &
