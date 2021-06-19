package kpn.server.analyzer.engine.changes.changes

import kpn.api.base.WithId

case class RouteElements(
  _id: Long,
  routeId: Long,
  elementIds: ElementIds
) extends WithId
