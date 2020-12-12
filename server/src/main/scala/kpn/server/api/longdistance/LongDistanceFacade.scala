package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangePage
import kpn.api.common.longdistance.LongDistanceRouteChangesPage
import kpn.api.common.longdistance.LongDistanceRouteMapPage
import kpn.api.common.longdistance.LongDistanceRouteDetailsPage
import kpn.api.common.longdistance.LongDistanceRoutesPage
import kpn.api.custom.ApiResponse

trait LongDistanceFacade {

  def routes(user: Option[String]): ApiResponse[LongDistanceRoutesPage]

  def route(user: Option[String], routeId: Long): ApiResponse[LongDistanceRouteDetailsPage]

  def routeMap(user: Option[String], routeId: Long): ApiResponse[LongDistanceRouteMapPage]

  def routeChanges(user: Option[String], routeId: Long): ApiResponse[LongDistanceRouteChangesPage]

  def routeChange(user: Option[String], routeId: Long, changeSetId: Long): ApiResponse[LongDistanceRouteChangePage]
}
