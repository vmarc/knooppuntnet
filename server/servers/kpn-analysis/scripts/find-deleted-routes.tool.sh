#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=find-deleted-routes-tool \
  -cp /kpn/bin/dist/server-4.0.0-snapshot.39.jar \
  -Dlog4j.configurationFile=/kpn/scripts/conf/find-deleted-routes-tool-log.xml \
  -Dloader.main=kpn.core.tools.support.FindDeletedRoutesTool org.springframework.boot.loader.PropertiesLauncher \
  kpn-3 > /kpn/logs/find-deleted-routes-tool-stdout.log 2>&1 &
