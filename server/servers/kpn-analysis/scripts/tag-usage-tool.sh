#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -cp /kpn/server-2.0.20-SNAPSHOT/lib/core.core-*.jar:/kpn/server-2.0.20-SNAPSHOT/lib/* \
  -Dcom.sun.management.jmxremote.ssl=false \
  kpn.core.tools.TagUsageTool knooppuntnet.server master1 >> /kpn/logs/tag-usage-tool.log 2>&1 &
