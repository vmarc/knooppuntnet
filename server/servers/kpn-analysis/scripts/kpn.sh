#!/usr/bin/env bash
/kpn/java/bin/java \
  -Dname=operation-tool \
  -Xms512M \
  -Xmx4G \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.operation.OperationTool \
  org.springframework.boot.loader.PropertiesLauncher
