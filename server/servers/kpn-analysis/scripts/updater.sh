#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=updater \
  -Dlog4j.configurationFile=/kpn/scripts/conf/updater-log.xml \
  -Dcom.sun.management.jmxremote.port=5103 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx512M \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.replicate.UpdaterTool org.springframework.boot.loader.PropertiesLauncher \
  --actions-database backend-actions \
  --rootDir /kpn >> /kpn/logs/updater-stdout.log 2>&1 &
