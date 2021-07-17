package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.data.Node
import kpn.api.custom.Country

case class OldNodeAnalysis(
  node: Node,
  country: Option[Country],
  locations: Seq[String]
)
