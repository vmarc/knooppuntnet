package kpn.api.common.route

import kpn.server.analyzer.engine.changes.changes.ElementIds

case class RouteElements(
  routeId: Long,
  elementIds: ElementIds
)
