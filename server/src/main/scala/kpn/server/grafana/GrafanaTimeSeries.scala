package kpn.server.grafana

case class GrafanaTimeSeries(
  target: String,
  datapoints: Seq[(Float,Long)]
)
