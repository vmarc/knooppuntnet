package kpn.server.repository

import kpn.api.common.location.LocationFact
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.LocationNodesType
import kpn.api.custom.LocationRoutesType
import kpn.api.custom.NetworkType
import kpn.core.mongo.doc.LocationNodeCount

trait LocationRepository {

  def summary(locationKey: LocationKey): LocationSummary

  def nodes(locationKey: LocationKey, parameters: LocationNodesParameters): Seq[LocationNodeInfo]

  def nodeCount(locationKey: LocationKey, locationNodesType: LocationNodesType): Long

  def routes(locationKey: LocationKey, parameters: LocationRoutesParameters): Seq[LocationRouteInfo]

  def routeCount(locationKey: LocationKey, locationRoutesType: LocationRoutesType): Long

  def countryLocations(networkType: NetworkType, country: Country): Seq[LocationNodeCount]

  def facts(networkType: NetworkType, locationName: String): Seq[LocationFact]

  def factCount(networkType: NetworkType, locationName: String): Long

}
