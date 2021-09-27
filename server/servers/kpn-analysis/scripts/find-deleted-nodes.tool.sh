#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=find-deleted-nodes-tool \
  -cp /kpn/bin/server.jar \
  -Dlog4j.configurationFile=/kpn/scripts/conf/find-deleted-nodes-tool-log.xml \
  -Dloader.main=kpn.core.tools.support.FindDeletedNodesTool org.springframework.boot.loader.PropertiesLauncher \
  kpn-database attic-analysis > /kpn/logs/find-deleted-nodes-tool-stdout.log 2>&1 &
