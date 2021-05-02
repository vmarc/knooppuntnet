package kpn.server.repository

import kpn.api.common.common.Ref
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
import kpn.core.database.Database
import kpn.core.database.views.location.LocationFactView
import kpn.core.database.views.location.LocationNodeCount
import kpn.core.database.views.location.LocationNodeView
import kpn.core.database.views.location.LocationRouteView
import kpn.core.database.views.location.LocationView
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(analysisDatabase: Database) extends LocationRepository {

  override def summary(locationKey: LocationKey): LocationSummary = {
    LocationSummary(
      factCount(locationKey.networkType, locationKey.name),
      nodeCount(locationKey, LocationNodesType.all),
      routeCount(locationKey, LocationRoutesType.all),
      0
    )
  }

  override def routesWithoutLocation(networkType: NetworkType): Seq[Ref] = {
    LocationView.query(analysisDatabase, "route-without-location", networkType, "").sortBy(_.name)
  }

  override def nodes(locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean): Seq[LocationNodeInfo] = {
    LocationNodeView.query(analysisDatabase, locationKey, parameters, stale)
  }

  override def nodeCount(locationKey: LocationKey, locationNodesType: LocationNodesType, stale: Boolean): Long = {
    LocationNodeView.queryCount(analysisDatabase, locationKey, locationNodesType, stale)
  }

  override def routes(locationKey: LocationKey, parameters: LocationRoutesParameters, stale: Boolean = true): Seq[LocationRouteInfo] = {
    LocationRouteView.query(analysisDatabase, locationKey, parameters, stale)
  }

  override def routeCount(locationKey: LocationKey, locationRoutesType: LocationRoutesType, stale: Boolean = true): Long = {
    LocationRouteView.queryCount(analysisDatabase, locationKey, locationRoutesType, stale)
  }

  override def countryLocations(networkType: NetworkType, country: Country, stale: Boolean = true): Seq[LocationNodeCount] = {
    LocationNodeView.countryLocations(analysisDatabase, networkType, country, stale)
  }

  override def facts(networkType: NetworkType, locationName: String, stale: Boolean = true): Seq[LocationFact] = {
    LocationFactView.query(analysisDatabase, networkType, locationName, stale)
  }

  override def factCount(networkType: NetworkType, locationName: String, stale: Boolean = true): Long = {
    LocationFactView.queryCount(analysisDatabase, networkType, locationName, stale)
  }

}
