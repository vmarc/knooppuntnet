#!/usr/bin/env bash
/kpn/java/bin/java \
  -Dname=operation-tool \
  -Xms512M \
  -Xmx4G \
  -cp /kpn/bin/dist/server-3.0.0-SNAPSHOT-alpha-8.jar \
  -Dloader.main=kpn.core.tools.operation.OperationTool \
  org.springframework.boot.loader.PropertiesLauncher
