package kpn.server.analyzer.engine.changes.changes

import kpn.server.analyzer.engine.context.ElementIds

case class ReferencedElementIds(
  _id: Long,
  elementIds: ElementIds
)
