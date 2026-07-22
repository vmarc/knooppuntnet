package kpn.server.grafana

import kpn.api.id.Storable

case class GrafanaQueryTarget(
  refId: String,
  payload: String,
  target: String,
  datasource: String
) extends Storable
