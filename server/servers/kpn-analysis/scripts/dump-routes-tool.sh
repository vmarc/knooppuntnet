#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=dump-routes-tool \
  -cp /kpn/bin/dist/server-3.1.6-dump-routes.jar \
  -Dlog4j.configurationFile=/kpn/conf/dump-routes-tool-log.xml \
  -Dloader.main=kpn.core.tools.support.DumpRoutesTool org.springframework.boot.loader.PropertiesLauncher \
  kpn-database attic-analysis > /kpn/logs/dump-routes-tool-stdout.log 2>&1 &
