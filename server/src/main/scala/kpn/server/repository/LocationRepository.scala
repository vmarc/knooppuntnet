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
import kpn.server.analyzer.engine.analysis.location.LocationFilter

trait LocationRepository {

  def summary(locationFilter: LocationFilter): LocationSummary

  def nodes(locationFilter: LocationFilter, parameters: LocationNodesParameters): Seq[LocationNodeInfo]

  def nodeCount(locationFilter: LocationFilter, parameters: LocationNodesParameters): Long

  def routes(locationFilter: LocationFilter, parameters: LocationRoutesParameters): Seq[LocationRouteInfo]

  def filterOptions(locationFilter: LocationFilter, parameters: LocationRoutesParameters): LocationRouteOptions

  def routeCount(locationFilter: LocationFilter): Long

  def routeFilteredCount(locationFilter: LocationFilter, parameters: LocationRoutesParameters): Long

  def countryLocations(networkType: NetworkType, country: Country): Seq[LocationNodeCount]

  def facts(locationFilter: LocationFilter): Seq[LocationFact]

  def factCount(locationFilter: LocationFilter): Long

  def changes(locationFilter: LocationFilter, parameters: ChangesParameters): Seq[LocationChangeSet]

  def changesFilter(locationFilter: LocationFilter, parameters: ChangesParameters): Seq[ChangesFilterOption]

  def changesCount(locationFilter: LocationFilter, parameters: ChangesParameters): Long
}
