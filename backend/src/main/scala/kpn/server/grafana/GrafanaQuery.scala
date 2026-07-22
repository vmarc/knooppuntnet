package kpn.server.grafana

import kpn.api.id.Storable

case class GrafanaQuery(
  targets: Seq[GrafanaQueryTarget],
  startTime: Long,
  intervalMs: Long,
  maxDataPoints: Long
) extends Storable
