#!/usr/bin/env bash
/kpn/java/bin/java \
  -Dname=poi-tile-clean-tool \
  -Xms512M \
  -Xmx4G \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.server.analyzer.engine.tiles.PoiTileCleanTool org.springframework.boot.loader.PropertiesLauncher
