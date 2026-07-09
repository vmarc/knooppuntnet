package kpn.core.tools.next.domain

import kpn.core.doc.WithId
import kpn.server.analyzer.engine.context.ElementIds

case class NextRouteState(
  _id: Long, // relationId
  tiles: Seq[String],
  elementIds: ElementIds,
) extends WithId
