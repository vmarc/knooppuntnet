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
import kpn.core.database.views.location.LocationFactView
import kpn.core.database.views.location.LocationNodeCount
import kpn.core.database.views.location.LocationNodeView
import kpn.core.database.views.location.LocationRouteView
import kpn.core.mongo.Database
import kpn.core.mongo.actions.locations.MongoQueryLocationFactCount
import kpn.core.mongo.actions.locations.MongoQueryLocationFacts
import kpn.core.mongo.actions.locations.MongoQueryLocationNodeCounts
import kpn.core.mongo.actions.locations.MongoQueryLocationNodes
import kpn.core.mongo.actions.locations.MongoQueryLocationRoutes
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends LocationRepository {

  override def summary(locationKey: LocationKey): LocationSummary = {
    if (mongoEnabled) {
      LocationSummary(
        factCount(locationKey.networkType, locationKey.name),
        nodeCount(locationKey, LocationNodesType.all),
        routeCount(locationKey, LocationRoutesType.all),
        0
      )
    }
    else {
      LocationSummary(
        factCount(locationKey.networkType, locationKey.name),
        nodeCount(locationKey, LocationNodesType.all),
        routeCount(locationKey, LocationRoutesType.all),
        0
      )
    }
  }

  override def nodes(locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean): Seq[LocationNodeInfo] = {
    if (mongoEnabled) {
      new MongoQueryLocationNodes(database).find(
        locationKey.networkType,
        locationKey.name,
        parameters.locationNodesType,
        parameters.pageIndex.toInt,
        parameters.itemsPerPage.toInt
      )
    }
    else {
      LocationNodeView.query(analysisDatabase, locationKey, parameters, stale)
    }
  }

  override def nodeCount(locationKey: LocationKey, locationNodesType: LocationNodesType, stale: Boolean): Long = {
    if (mongoEnabled) {
      new MongoQueryLocationNodes(database).countDocuments(locationKey.networkType, locationKey.name, locationNodesType)
    }
    else {
      LocationNodeView.queryCount(analysisDatabase, locationKey, locationNodesType, stale)
    }
  }

  override def routes(locationKey: LocationKey, parameters: LocationRoutesParameters, stale: Boolean = true): Seq[LocationRouteInfo] = {
    if (mongoEnabled) {
      new MongoQueryLocationRoutes(database).find(
        locationKey.networkType,
        locationKey.name,
        parameters.locationRoutesType,
        parameters.pageIndex.toInt,
        parameters.itemsPerPage.toInt
      )
    }
    else {
      LocationRouteView.query(analysisDatabase, locationKey, parameters, stale)
    }
  }

  override def routeCount(locationKey: LocationKey, locationRoutesType: LocationRoutesType, stale: Boolean = true): Long = {
    if (mongoEnabled) {
      new MongoQueryLocationRoutes(database).countDocuments(
        locationKey.networkType,
        locationKey.name,
        locationRoutesType
      )
    }
    else {
      LocationRouteView.queryCount(analysisDatabase, locationKey, locationRoutesType, stale)
    }
  }

  override def countryLocations(networkType: NetworkType, country: Country, stale: Boolean = true): Seq[LocationNodeCount] = {
    if (mongoEnabled) {
      new MongoQueryLocationNodeCounts(database).find(
        networkType,
        country
      )
    }
    else {
      LocationNodeView.countryLocations(analysisDatabase, networkType, country, stale)
    }
  }

  override def facts(networkType: NetworkType, locationName: String, stale: Boolean = true): Seq[LocationFact] = {
    if (mongoEnabled) {
      new MongoQueryLocationFacts(database).execute(networkType, locationName)
    }
    else {
      LocationFactView.query(analysisDatabase, networkType, locationName, stale)
    }
  }

  override def factCount(networkType: NetworkType, locationName: String, stale: Boolean = true): Long = {
    if (mongoEnabled) {
      new MongoQueryLocationFactCount(database).execute(networkType, locationName)
    }
    else {
      LocationFactView.queryCount(analysisDatabase, networkType, locationName, stale)
    }
  }
}
