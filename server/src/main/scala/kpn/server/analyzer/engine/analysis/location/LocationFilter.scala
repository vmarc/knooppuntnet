package kpn.server.analyzer.engine.analysis.location

import kpn.api.custom.NetworkType

case class LocationFilter(
  networkType: NetworkType,
  locationIds: Seq[String]
)
