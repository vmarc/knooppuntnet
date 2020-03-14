#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=country \
  -cp /kpn/bin/tmp/server-2.0.21.4/lib/core.core-*.jar:/kpn/bin/tmp/server-2.0.21.4/lib/* \
  -Dlog4j.configurationFile=/kpn/conf/country-log.xml \
  kpn.core.tools.country.CountryBoundaryTool >> /kpn/logs/country-stdout.log 2>&1 &
