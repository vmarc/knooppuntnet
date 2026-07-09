package kpn.server.repository

import kpn.api.common.LocationChangeSet
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.location.LocationFact
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodeOptions
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRouteOptions
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Subset
import kpn.database.actions.locations.LocationQueryResult
import kpn.database.actions.locations.MongoQueryLocationChanges
import kpn.database.actions.locations.MongoQueryLocationFactCount
import kpn.database.actions.locations.MongoQueryLocationFacts
import kpn.database.actions.locations.MongoQueryLocationNodes
import kpn.database.actions.locations.MongoQueryLocationRoutes
import kpn.database.actions.locations.MongoQueryLocations
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class LocationRepository(database: Database) {

  def summary(subset: LocationSubset): LocationSummary = {
    LocationSummary(
      factCount(subset),
      nodeCount(subset, LocationNodesParameters()),
      routeCount(subset),
      changesCount(subset, ChangesParameters())
    )
  }

  def distance(subset: LocationSubset): Long = {
    new MongoQueryLocations(database).distance(subset)
  }

  def nodes(subset: LocationSubset, parameters: LocationNodesParameters): Seq[LocationNodeInfo] = {
    new MongoQueryLocationNodes(database, SurveyDateInfoBuilder.dateInfo).find(
      subset,
      parameters
    )
  }

  def nodeFilterOptions(subset: LocationSubset, parameters: LocationNodesParameters): LocationNodeOptions = {
    new MongoQueryLocationNodes(database, SurveyDateInfoBuilder.dateInfo).filterOptions(
      subset,
      parameters
    )
  }

  def nodeCount(subset: LocationSubset, parameters: LocationNodesParameters): Long = {
    new MongoQueryLocationNodes(database, SurveyDateInfoBuilder.dateInfo).countDocuments(subset, parameters)
  }

  def routes(subset: LocationSubset, parameters: LocationRoutesParameters): Seq[LocationRouteInfo] = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).find(
      subset,
      parameters
    )
  }

  def routeFilterOptions(subset: LocationSubset, parameters: LocationRoutesParameters): LocationRouteOptions = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).filterOptions(
      subset,
      parameters
    )
  }

  def routeCount(subset: LocationSubset): Long = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).countDocuments(
      subset,
      LocationRoutesParameters()
    )
  }

  def routeFilteredCount(subset: LocationSubset, parameters: LocationRoutesParameters): Long = {
    new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo).countDocuments(
      subset,
      parameters
    )
  }

  def countryLocations(subset: Subset): Seq[LocationQueryResult] = {
    new MongoQueryLocations(database).execute(subset)
  }

  def facts(subset: LocationSubset): Seq[LocationFact] = {
    new MongoQueryLocationFacts(database).execute(subset)
  }

  def factCount(subset: LocationSubset): Long = {
    new MongoQueryLocationFactCount(database).execute(subset)
  }

  def changes(subset: LocationSubset, parameters: ChangesParameters): Seq[LocationChangeSet] = {
    new MongoQueryLocationChanges(database).execute(subset, parameters)
  }

  def changesFilter(subset: LocationSubset, parameters: ChangesParameters): Seq[ChangesFilterOption] = {
    val changeSetCounts = new MongoQueryLocationChanges(database).executeFilterOptions(subset, parameters)
    changeSetCounts.toFilterOptions(parameters.year, parameters.month, parameters.day)
  }

  def changesCount(subset: LocationSubset, parameters: ChangesParameters): Long = {
    new MongoQueryLocationChanges(database).executeCount(subset, parameters)
  }
}
