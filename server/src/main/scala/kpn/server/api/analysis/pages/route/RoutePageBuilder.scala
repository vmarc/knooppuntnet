package kpn.server.api.analysis.pages.route

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteChangesPage
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteMapPage

trait RoutePageBuilder {

  def buildDetailsPage(user: Option[String], routeId: Long): Option[RouteDetailsPage]

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage]

  def buildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage]
}
