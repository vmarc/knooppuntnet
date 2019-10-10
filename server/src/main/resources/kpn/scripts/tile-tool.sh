#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/tile-tool-log.xml \
  kpn.core.tools.TileTool -t /kpn/tiles -a master1 >> /kpn/logs/tile-tool-stdout.log 2>&1 &
