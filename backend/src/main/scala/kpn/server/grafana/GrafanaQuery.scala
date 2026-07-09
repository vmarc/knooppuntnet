package kpn.server.grafana

import kpn.core.doc.Storable

case class GrafanaQuery(
  targets: Seq[GrafanaQueryTarget],
  startTime: Long,
  intervalMs: Long,
  maxDataPoints: Long
) extends Storable
