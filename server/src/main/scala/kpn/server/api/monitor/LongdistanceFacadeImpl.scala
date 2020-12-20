package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteChangePage
import kpn.api.common.monitor.LongdistanceRouteChangesPage
import kpn.api.common.monitor.LongdistanceRouteDetailsPage
import kpn.api.common.monitor.LongdistanceRouteMapPage
import kpn.api.common.monitor.LongdistanceRoutesPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.api.monitor.longdistance.LongdistanceRouteChangePageBuilder
import kpn.server.api.monitor.longdistance.LongdistanceRouteChangesPageBuilder
import kpn.server.api.monitor.longdistance.LongdistanceRouteDetailsPageBuilder
import kpn.server.api.monitor.longdistance.LongdistanceRouteMapPageBuilder
import kpn.server.api.monitor.longdistance.LongdistanceRoutesPageBuilder
import org.springframework.stereotype.Component

@Component
class LongdistanceFacadeImpl(
  api: Api,
  longdistanceRoutesPageBuilder: LongdistanceRoutesPageBuilder,
  longdistanceRouteDetailsPageBuilder: LongdistanceRouteDetailsPageBuilder,
  longdistanceRouteMapPageBuilder: LongdistanceRouteMapPageBuilder,
  longdistanceRouteChangesPageBuilder: LongdistanceRouteChangesPageBuilder,
  longdistanceRouteChangePageBuilder: LongdistanceRouteChangePageBuilder

) extends LongdistanceFacade {

  def longdistanceRoutes(user: Option[String]): ApiResponse[LongdistanceRoutesPage] = {
    api.execute(user, "longdistance-routes", "") {
      reply(longdistanceRoutesPageBuilder.build())
    }
  }

  def longdistanceRoute(user: Option[String], routeId: Long): ApiResponse[LongdistanceRouteDetailsPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "longdistance-route", args) {
      reply(longdistanceRouteDetailsPageBuilder.build(routeId))
    }
  }

  def longdistanceRouteMap(user: Option[String], routeId: Long): ApiResponse[LongdistanceRouteMapPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "longdistance-route-map", args) {
      reply(longdistanceRouteMapPageBuilder.build(routeId))
    }
  }

  def longdistanceRouteChanges(user: Option[String], routeId: Long): ApiResponse[LongdistanceRouteChangesPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "longdistance-route-changes", args) {
      reply(longdistanceRouteChangesPageBuilder.build(routeId))
    }
  }

  def longdistanceRouteChange(user: Option[String], routeId: Long, changeSetId: Long): ApiResponse[LongdistanceRouteChangePage] = {
    val args = s"routeId=$routeId, changeSetId$changeSetId"
    api.execute(user, "longdistance-route-change", args) {
      reply(longdistanceRouteChangePageBuilder.build(routeId, changeSetId))
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }

}
