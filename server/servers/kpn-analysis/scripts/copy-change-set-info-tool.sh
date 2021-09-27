#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dlog4j.configurationFile=/kpn/conf/copy-change-set-info-tool-log.xml \
  -cp /kpn/server-2.0.14-SNAPSHOT/lib/core.core-*.jar:/kpn/server-2.0.14-SNAPSHOT/lib/* \
  kpn.core.tools.support.CopyChangesetInfoTool > /kpn/logs/copy-change-set-info-tool-stdout.log 2>&1 &
