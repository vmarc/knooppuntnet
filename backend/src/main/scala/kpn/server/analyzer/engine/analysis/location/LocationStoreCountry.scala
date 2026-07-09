package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Country

case class LocationStoreCountry(
  country: Country,
  dataMap: Map[String, LocationStoreData],
  tree: LocationStoreData
)
