package kpn.api.common.network

import kpn.server.analyzer.engine.changes.changes.ElementIds

case class NetworkElements(
  networkId: Long,
  elementsIds: ElementIds
)
