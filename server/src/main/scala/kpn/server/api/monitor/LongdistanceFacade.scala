package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteChangePage
import kpn.api.common.monitor.LongdistanceRouteChangesPage
import kpn.api.common.monitor.LongdistanceRouteDetailsPage
import kpn.api.common.monitor.LongdistanceRouteMapPage
import kpn.api.common.monitor.LongdistanceRoutesPage
import kpn.api.custom.ApiResponse

trait LongdistanceFacade {

  def longdistanceRoutes(user: Option[String]): ApiResponse[LongdistanceRoutesPage]

  def longdistanceRoute(user: Option[String], routeId: Long): ApiResponse[LongdistanceRouteDetailsPage]

  def longdistanceRouteMap(user: Option[String], routeId: Long): ApiResponse[LongdistanceRouteMapPage]

  def longdistanceRouteChanges(user: Option[String], routeId: Long): ApiResponse[LongdistanceRouteChangesPage]

  def longdistanceRouteChange(user: Option[String], routeId: Long, changeSetId: Long): ApiResponse[LongdistanceRouteChangePage]

}
