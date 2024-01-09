#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=find-deleted-routes-tool \
  -cp /kpn/bin/server.jar \
  -Dlog4j.configurationFile=/kpn/scripts/conf/find-deleted-routes-tool-log.xml \
  -Dloader.main=kpn.core.tools.support.FindDeletedRoutesTool org.springframework.boot.loader.launch.PropertiesLauncher \
  kpn-3 > /kpn/logs/find-deleted-routes-tool-stdout.log 2>&1 &
