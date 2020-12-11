package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangeSetPage
import kpn.api.common.longdistance.LongDistanceRouteChangesPage
import kpn.api.common.longdistance.LongDistanceRouteMapPage
import kpn.api.common.longdistance.LongDistanceRouteDetailsPage
import kpn.api.common.longdistance.LongDistanceRoutesPage
import kpn.api.custom.ApiResponse

trait LongDistanceFacade {

  def routes(): ApiResponse[LongDistanceRoutesPage]

  def route(routeId: Long): ApiResponse[LongDistanceRouteDetailsPage]

  def routeMap(routeId: Long): ApiResponse[LongDistanceRouteMapPage]

  def routeChanges(routeId: Long): ApiResponse[LongDistanceRouteChangesPage]

  def routeChange(changeSetId: Long, routeId: Long): ApiResponse[LongDistanceRouteChangeSetPage]
}
