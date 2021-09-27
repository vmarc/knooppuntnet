#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=migrate-changes-tool \
  -cp /kpn/bin/dist/server-3.2-snapshot-10.jar \
  -Dlog4j.configurationFile=/kpn/conf/migrate-changes-tool-log.xml \
  -Dloader.main=kpn.core.mongo.migration.MigrateChangeKeyTool org.springframework.boot.loader.PropertiesLauncher \
  > /kpn/logs/migrate-changes-tool-stdout.log 2>&1 &
