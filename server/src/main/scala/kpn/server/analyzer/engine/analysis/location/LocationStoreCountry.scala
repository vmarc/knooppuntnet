package kpn.server.analyzer.engine.analysis.location

import kpn.api.custom.Country

case class LocationStoreCountry(
  country: Country,
  dataMap: Map[String, LocationStoreData]
)
