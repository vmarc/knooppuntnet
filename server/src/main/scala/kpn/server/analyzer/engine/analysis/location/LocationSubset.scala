package kpn.server.analyzer.engine.analysis.location

import kpn.api.custom.NetworkType

case class LocationSubset(
  name: String,
  networkType: NetworkType,
  locationIds: Seq[String]
)
