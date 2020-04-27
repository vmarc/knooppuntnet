package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.data.Node
import kpn.api.common.location.Location
import kpn.api.custom.Country

case class NodeAnalysis(
  node: Node,
  country: Option[Country],
  location: Option[Location]
)
