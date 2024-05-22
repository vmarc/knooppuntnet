package kpn.server.analyzer.engine.analysis.location

import kpn.api.custom.NetworkType

case class LocationSubset(
  networkType: NetworkType,
  locationIds: Seq[String]
)
