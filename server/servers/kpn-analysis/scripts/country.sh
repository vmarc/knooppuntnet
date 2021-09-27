#!/usr/bin/env bash
/kpn/java/bin/java \
  -Dname=country \
  -Dlog4j.configurationFile=/kpn/scripts/conf/country-log.xml \
  -Xms512M \
  -Xmx4G \
  -cp /kpn/bin/dist/server-3.0.0-SNAPSHOT-alpha-43-a.jar \
  -Dloader.main=kpn.core.tools.country.CountryBoundaryTool org.springframework.boot.loader.PropertiesLauncher
