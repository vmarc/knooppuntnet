package kpn.server.grafana

case class GrafanaQuery(
  targets: Seq[GrafanaQueryTarget],
  startTime: Long,
  intervalMs: Long,
  maxDataPoints: Long
)
