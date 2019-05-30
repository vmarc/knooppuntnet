package kpn.core.facade.pages

import kpn.shared.route.RouteChangesPage
import kpn.shared.route.RouteDetailsPage
import kpn.shared.route.RouteMapPage
import kpn.shared.route.RoutePage

trait RoutePageBuilder {

  def build(user: Option[String], routeId: Long): Option[RoutePage]

  def buildDetailsPage(user: Option[String], routeId: Long): Option[RouteDetailsPage]

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage]

  def buildChangesPage(user: Option[String], routeId: Long, itemsPerPage: Int, pageIndex: Int): Option[RouteChangesPage]
}
