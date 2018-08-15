#!/usr/bin/env bash
. /kpn/conf/kpn-env.sh
${JAVA_HOME}/bin/java \
  -cp /kpn/app/lib/core-core*.jar:/kpn/app/lib/* \
  kpn.core.tools.operation.OperationTool
