package kpn.server.grafana

import kpn.core.doc.Storable

case class GrafanaQueryTarget(
  refId: String,
  payload: String,
  target: String,
  datasource: String
) extends Storable
