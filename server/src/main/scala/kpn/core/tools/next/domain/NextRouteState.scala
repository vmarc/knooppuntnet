package kpn.core.tools.next.domain

import kpn.api.base.WithId
import kpn.server.analyzer.engine.context.ElementIds

case class NextRouteState(
  _id: Long, // relationId
  tiles: Seq[String],
  elementIds: ElementIds,
) extends WithId
