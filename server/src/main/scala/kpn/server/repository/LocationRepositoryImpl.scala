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
import kpn.api.custom.NetworkType
import kpn.core.doc.LocationNodeCount
import kpn.database.actions.locations.MongoQueryLocationChanges
import kpn.database.actions.locations.MongoQueryLocationFactCount
import kpn.database.actions.locations.MongoQueryLocationFacts
import kpn.database.actions.locations.MongoQueryLocationNodeCounts
import kpn.database.actions.locations.MongoQueryLocationNodes
import kpn.database.actions.locations.MongoQueryLocationRoutes
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationFilter
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(database: Database) extends LocationRepository {

  override def summary(locationFilter: LocationFilter): LocationSummary = {
    LocationSummary(
      factCount(locationFilter),
      nodeCount(locationFilter, LocationNodesParameters()),
      routeCount(locationFilter),
      changesCount(locationFilter, ChangesParameters())
    )
  }

  override def nodes(locationFilter: LocationFilter, parameters: LocationNodesParameters): Seq[LocationNodeInfo] = {
    new MongoQueryLocationNodes(database).find(
      locationFilter,
      parameters
    )
  }

  override def nodeCount(locationFilter: LocationFilter, parameters: LocationNodesParameters): Long = {
    new MongoQueryLocationNodes(database).countDocuments(locationFilter, parameters)
  }

  override def routes(locationFilter: LocationFilter, parameters: LocationRoutesParameters): Seq[LocationRouteInfo] = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).find(
      locationFilter,
      parameters
    )
  }

  override def filterOptions(locationFilter: LocationFilter, parameters: LocationRoutesParameters): LocationRouteOptions = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).filterOptions(
      locationFilter,
      parameters
    )
  }

  override def routeCount(locationFilter: LocationFilter): Long = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).countDocuments(
      locationFilter,
      LocationRoutesParameters()
    )
  }

  override def routeFilteredCount(locationFilter: LocationFilter, parameters: LocationRoutesParameters): Long = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).countDocuments(
      locationFilter,
      parameters
    )
  }

  override def countryLocations(networkType: NetworkType, country: Country): Seq[LocationNodeCount] = {
    new MongoQueryLocationNodeCounts(database).find(
      networkType,
      country
    )
  }

  override def facts(locationFilter: LocationFilter): Seq[LocationFact] = {
    new MongoQueryLocationFacts(database).execute(locationFilter)
  }

  override def factCount(locationFilter: LocationFilter): Long = {
    new MongoQueryLocationFactCount(database).execute(locationFilter)
  }

  override def changes(locationFilter: LocationFilter, parameters: ChangesParameters): Seq[LocationChangeSet] = {
    new MongoQueryLocationChanges(database).execute(locationFilter, parameters)
  }

  override def changesFilter(locationFilter: LocationFilter, parameters: ChangesParameters): Seq[ChangesFilterOption] = {
    val changeSetCounts = new MongoQueryLocationChanges(database).executeFilterOptions(locationFilter, parameters)
    changeSetCounts.toFilterOptions(parameters.year, parameters.month, parameters.day)
  }

  override def changesCount(locationFilter: LocationFilter, parameters: ChangesParameters): Long = {
    new MongoQueryLocationChanges(database).executeCount(locationFilter, parameters)
  }
}
