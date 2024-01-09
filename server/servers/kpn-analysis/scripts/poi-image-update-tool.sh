#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=poi-image-update-tool \
  -Dlog4j.configurationFile=/kpn/scripts/conf/poi-image-update-tool-log.xml \
  -Xms128M \
  -Xmx4G \
  -cp /kpn/bin/dist/server-4.0.1.jar \
  -Dloader.main=kpn.server.analyzer.engine.poi.image.PoiImageUpdateTool org.springframework.boot.loader.launch.PropertiesLauncher \
  > /kpn/logs/poi-image-update-tool-stdout.log 2>&1 &
