#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=dump-routes-tool \
  -cp /kpn/bin/server.jar \
  -Dlog4j.configurationFile=/kpn/scripts/conf/dump-routes-tool-log.xml \
  -Dloader.main=kpn.core.tools.support.DumpRoutesTool org.springframework.boot.loader.PropertiesLauncher \
  kpn-frontend attic-analysis > /kpn/logs/dump-routes-tool-stdout.log 2>&1 &
