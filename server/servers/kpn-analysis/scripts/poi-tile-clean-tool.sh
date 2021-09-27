#!/usr/bin/env bash
/kpn/java/bin/java \
  -Dname=poi-tile-clean-tool \
  -Xms512M \
  -Xmx4G \
  -cp /kpn/bin/dist/server-3.0.0-SNAPSHOT-alpha-4.jar \
  -Dloader.main=kpn.server.analyzer.engine.tiles.PoiTileCleanTool org.springframework.boot.loader.PropertiesLauncher
