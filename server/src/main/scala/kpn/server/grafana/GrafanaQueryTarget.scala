package kpn.server.grafana

case class GrafanaQueryTarget(
  refId: String,
  payload: String,
  target: String,
  datasource: String
)
