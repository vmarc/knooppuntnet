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
import kpn.core.database.views.location.LocationNodeCount
import kpn.core.mongo.Database
import kpn.core.mongo.actions.locations.MongoQueryLocationFactCount
import kpn.core.mongo.actions.locations.MongoQueryLocationFacts
import kpn.core.mongo.actions.locations.MongoQueryLocationNodeCounts
import kpn.core.mongo.actions.locations.MongoQueryLocationNodes
import kpn.core.mongo.actions.locations.MongoQueryLocationRoutes
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(database: Database) extends LocationRepository {

  override def summary(locationKey: LocationKey): LocationSummary = {
    LocationSummary(
      factCount(locationKey.networkType, locationKey.name),
      nodeCount(locationKey, LocationNodesType.all),
      routeCount(locationKey, LocationRoutesType.all),
      0
    )
  }

  override def nodes(locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean): Seq[LocationNodeInfo] = {
    new MongoQueryLocationNodes(database).find(
      locationKey.networkType,
      locationKey.name,
      parameters.locationNodesType,
      parameters.pageIndex.toInt,
      parameters.itemsPerPage.toInt
    )
  }

  override def nodeCount(locationKey: LocationKey, locationNodesType: LocationNodesType, stale: Boolean): Long = {
    new MongoQueryLocationNodes(database).countDocuments(locationKey.networkType, locationKey.name, locationNodesType)
  }

  override def routes(locationKey: LocationKey, parameters: LocationRoutesParameters, stale: Boolean = true): Seq[LocationRouteInfo] = {
    new MongoQueryLocationRoutes(database).find(
      locationKey.networkType,
      locationKey.name,
      parameters.locationRoutesType,
      parameters.pageIndex.toInt,
      parameters.itemsPerPage.toInt
    )
  }

  override def routeCount(locationKey: LocationKey, locationRoutesType: LocationRoutesType, stale: Boolean = true): Long = {
    new MongoQueryLocationRoutes(database).countDocuments(
      locationKey.networkType,
      locationKey.name,
      locationRoutesType
    )
  }

  override def countryLocations(networkType: NetworkType, country: Country, stale: Boolean = true): Seq[LocationNodeCount] = {
    new MongoQueryLocationNodeCounts(database).find(
      networkType,
      country
    )
  }

  override def facts(networkType: NetworkType, locationName: String, stale: Boolean = true): Seq[LocationFact] = {
    new MongoQueryLocationFacts(database).execute(networkType, locationName)
  }

  override def factCount(networkType: NetworkType, locationName: String, stale: Boolean = true): Long = {
    new MongoQueryLocationFactCount(database).execute(networkType, locationName)
  }
}
