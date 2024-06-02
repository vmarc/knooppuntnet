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
import kpn.server.analyzer.engine.analysis.location.LocationSubset

trait LocationRepository {

  def summary(subset: LocationSubset): LocationSummary

  def nodes(subset: LocationSubset, parameters: LocationNodesParameters): Seq[LocationNodeInfo]

  def nodeFilterOptions(subset: LocationSubset, parameters: LocationNodesParameters): LocationNodeOptions

  def nodeCount(subset: LocationSubset, parameters: LocationNodesParameters): Long

  def routes(subset: LocationSubset, parameters: LocationRoutesParameters): Seq[LocationRouteInfo]

  def routeFilterOptions(subset: LocationSubset, parameters: LocationRoutesParameters): LocationRouteOptions

  def routeCount(subset: LocationSubset): Long

  def routeFilteredCount(subset: LocationSubset, parameters: LocationRoutesParameters): Long

  def countryLocations(subset: Subset): Seq[LocationQueryResult]

  def facts(subset: LocationSubset): Seq[LocationFact]

  def factCount(subset: LocationSubset): Long

  def changes(subset: LocationSubset, parameters: ChangesParameters): Seq[LocationChangeSet]

  def changesFilter(subset: LocationSubset, parameters: ChangesParameters): Seq[ChangesFilterOption]

  def changesCount(subset: LocationSubset, parameters: ChangesParameters): Long
}
