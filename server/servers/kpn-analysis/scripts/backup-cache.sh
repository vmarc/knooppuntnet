#!/usr/bin/env bash
nohup /kpn/java/bin/java \
  -Dlog4j.configurationFile=/kpn/conf/backup-cache-log.xml \
  -Dconfig.file=/kpn/conf/production.conf \
  -cp /kpn/app/lib/core.core-*.jar:/kpn/app/lib/* \
  kpn.core.tools.backup.BackupTool \
  --localRoot /kpn/cache/ \
  --remoteRoot /cache-backup-files/ \
  --directory 2019 >> /kpn/logs/backup-cache-stdout.log 2>&1 &
