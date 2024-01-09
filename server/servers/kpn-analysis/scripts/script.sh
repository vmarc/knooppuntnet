#!/usr/bin/env bash
/kpn/java/bin/java \
  -Dname=script \
  -Xms512M \
  -Xmx4G \
  -cp /kpn/bin/server.jar \
  -Dloader.main=kpn.core.tools.support.ScriptTool \
  org.springframework.boot.loader.launch.PropertiesLauncher "$1" "$2"