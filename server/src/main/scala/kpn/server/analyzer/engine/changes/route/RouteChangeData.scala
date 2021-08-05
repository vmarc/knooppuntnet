package kpn.server.analyzer.engine.changes.route

import kpn.api.custom.Relation

case class RouteChangeData(
  routeId: Long,
  before: Option[Relation],
  after: Option[Relation]
)
