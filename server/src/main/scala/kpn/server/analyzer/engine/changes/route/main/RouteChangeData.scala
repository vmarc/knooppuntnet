package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.Relation

case class RouteChangeData(
  routeId: Long,
  before: Option[Relation],
  after: Option[Relation]
)
