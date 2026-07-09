package kpn.server.monitor.domain

import kpn.core.doc.WithId
import kpn.server.analyzer.engine.context.ElementIds

case class MonitorRelation(
  _id: Long, // relationId
  tiles: Seq[String],
  elementIds: ElementIds,
) extends WithId
