#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=elements-snapshot-tool \
  -Dlog4j.configurationFile=/kpn/conf/elements-snapshot-tool-log.xml \
  -cp /kpn/bin/dist/server-3.0.0-SNAPSHOT-alpha-14.jar \
  -Dloader.main=kpn.core.tools.support.ElementsSnapshotTool \
  org.springframework.boot.loader.PropertiesLauncher \
  >> /kpn/logs/elements-snapshot-tool-stdout.log 2>&1 &
