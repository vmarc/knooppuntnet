package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.core.database.views.location.LocationNodeCount

trait LocationRepository {

  def summary(locationKey: LocationKey): LocationSummary

  def routesWithoutLocation(networkType: NetworkType): Seq[Ref]

  def nodes(locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean = true): Seq[LocationNodeInfo]

  def nodeCount(locationKey: LocationKey, stale: Boolean = true): Long

  def routes(locationKey: LocationKey, parameters: LocationRoutesParameters, stale: Boolean = true): Seq[LocationRouteInfo]

  def routeCount(locationKey: LocationKey, stale: Boolean = true): Long

  def countryLocations(networkType: NetworkType, country: Country, stale: Boolean = true): Seq[LocationNodeCount]

}
