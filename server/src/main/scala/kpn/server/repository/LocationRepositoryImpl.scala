package kpn.server.repository

import kpn.api.common.LocationChangeSet
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.location.LocationFact
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRouteOptions
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.core.doc.LocationNodeCount
import kpn.database.actions.locations.MongoQueryLocationChanges
import kpn.database.actions.locations.MongoQueryLocationFactCount
import kpn.database.actions.locations.MongoQueryLocationFacts
import kpn.database.actions.locations.MongoQueryLocationNodeCounts
import kpn.database.actions.locations.MongoQueryLocationNodes
import kpn.database.actions.locations.MongoQueryLocationRoutes
import kpn.database.base.Database
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(database: Database) extends LocationRepository {

  override def summary(locationKey: LocationKey): LocationSummary = {
    LocationSummary(
      factCount(locationKey.networkType, locationKey.name),
      nodeCount(locationKey, LocationNodesParameters()),
      routeCount(locationKey),
      changesCount(locationKey, ChangesParameters())
    )
  }

  override def nodes(locationKey: LocationKey, parameters: LocationNodesParameters): Seq[LocationNodeInfo] = {
    new MongoQueryLocationNodes(database).find(
      locationKey,
      parameters
    )
  }

  override def nodeCount(locationKey: LocationKey, parameters: LocationNodesParameters): Long = {
    new MongoQueryLocationNodes(database).countDocuments(locationKey, parameters)
  }

  override def routes(locationKey: LocationKey, parameters: LocationRoutesParameters): Seq[LocationRouteInfo] = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).find(
      locationKey,
      parameters
    )
  }

  override def filterOptions(locationKey: LocationKey, parameters: LocationRoutesParameters): LocationRouteOptions = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).filterOptions(
      locationKey,
      parameters
    )
  }

  override def routeCount(locationKey: LocationKey): Long = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).countDocuments(
      locationKey,
      LocationRoutesParameters()
    )
  }

  override def routeFilteredCount(locationKey: LocationKey, parameters: LocationRoutesParameters): Long = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).countDocuments(
      locationKey,
      parameters
    )
  }

  override def countryLocations(networkType: NetworkType, country: Country): Seq[LocationNodeCount] = {
    new MongoQueryLocationNodeCounts(database).find(
      networkType,
      country
    )
  }

  override def facts(networkType: NetworkType, locationName: String): Seq[LocationFact] = {
    new MongoQueryLocationFacts(database).execute(networkType, locationName)
  }

  override def factCount(networkType: NetworkType, locationName: String): Long = {
    new MongoQueryLocationFactCount(database).execute(networkType, locationName)
  }

  override def changes(locationKey: LocationKey, parameters: ChangesParameters): Seq[LocationChangeSet] = {
    new MongoQueryLocationChanges(database).execute(locationKey.networkType, locationKey.name, parameters)
  }

  override def changesFilter(locationKey: LocationKey, parameters: ChangesParameters): Seq[ChangesFilterOption] = {
    val changeSetCounts = new MongoQueryLocationChanges(database).executeFilterOptions(locationKey.networkType, locationKey.name, parameters)
    changeSetCounts.toFilterOptions(parameters.year, parameters.month, parameters.day)
  }

  override def changesCount(locationKey: LocationKey, parameters: ChangesParameters): Long = {
    new MongoQueryLocationChanges(database).executeCount(locationKey.networkType, locationKey.name, parameters)
  }
}
