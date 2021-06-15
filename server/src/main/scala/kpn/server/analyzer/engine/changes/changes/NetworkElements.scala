package kpn.server.analyzer.engine.changes.changes

import kpn.api.base.WithId

case class NetworkElements(
  _id: Long, // networkId
  networkId: Long,
  elementsIds: ElementIds
) extends WithId
