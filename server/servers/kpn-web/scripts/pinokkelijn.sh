#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dname=pinokkelijn \
  -Dlog4j.configurationFile=/kpn/scripts/conf/pinokkelijn-log.xml \
  -jar /kpn/bin/pinokkelijn.jar \
  --spring.config.location=classpath:application.properties,file:/kpn/conf/pinokkelijn.properties >> /kpn/logs/pinokkelijn-stdout.log 2>&1 &
