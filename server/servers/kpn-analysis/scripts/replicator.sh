#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=replicator \
  -Dlog4j.configurationFile=/kpn/conf/replicator-log.xml \
  -Dcom.sun.management.jmxremote.port=5102 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Xms256M \
  -Xmx512M \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.replicate.ReplicatorTool org.springframework.boot.loader.PropertiesLauncher \
  --actions-database backend-actions \
  >> /kpn/logs/replicator-stdout.log 2>&1 &
