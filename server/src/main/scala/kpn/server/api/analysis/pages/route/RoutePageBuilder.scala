package kpn.server.api.analysis.pages.route

import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.route.RouteChangesPage
import kpn.shared.route.RouteDetailsPage
import kpn.shared.route.RouteMapPage
import kpn.shared.route.RoutePage

trait RoutePageBuilder {

  def build(user: Option[String], routeId: Long): Option[RoutePage] /* support for scalajs-react version */

  def buildDetailsPage(user: Option[String], routeId: Long): Option[RouteDetailsPage]

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage]

  def buildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage]
}
