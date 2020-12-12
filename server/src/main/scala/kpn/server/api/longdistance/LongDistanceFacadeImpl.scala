package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangePage
import kpn.api.common.longdistance.LongDistanceRouteChangesPage
import kpn.api.common.longdistance.LongDistanceRouteDetailsPage
import kpn.api.common.longdistance.LongDistanceRouteMapPage
import kpn.api.common.longdistance.LongDistanceRoutesPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import org.springframework.stereotype.Component

@Component
class LongDistanceFacadeImpl(
  api: Api,
  longDistanceRoutesPageBuilder: LongDistanceRoutesPageBuilder,
  longDistanceRouteDetailsPageBuilder: LongDistanceRouteDetailsPageBuilder,
  longDistanceRouteMapPageBuilder: LongDistanceRouteMapPageBuilder,
  longDistanceRouteChangesPageBuilder: LongDistanceRouteChangesPageBuilder,
  longDistanceRouteChangePageBuilder: LongDistanceRouteChangePageBuilder
) extends LongDistanceFacade {

  override def routes(user: Option[String]): ApiResponse[LongDistanceRoutesPage] = {
    api.execute(user, "long-distance-routes", "") {
      reply(longDistanceRoutesPageBuilder.build())
    }
  }

  override def route(user: Option[String], routeId: Long): ApiResponse[LongDistanceRouteDetailsPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "long-distance-route", args) {
      reply(longDistanceRouteDetailsPageBuilder.build(routeId))
    }
  }

  override def routeMap(user: Option[String], routeId: Long): ApiResponse[LongDistanceRouteMapPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "long-distance-route-map", args) {
      reply(longDistanceRouteMapPageBuilder.build(routeId))
    }
  }

  override def routeChanges(user: Option[String], routeId: Long): ApiResponse[LongDistanceRouteChangesPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "long-distance-route-changes", args) {
      reply(longDistanceRouteChangesPageBuilder.build(routeId))
    }
  }

  override def routeChange(user: Option[String], routeId: Long, changeSetId: Long): ApiResponse[LongDistanceRouteChangePage] = {
    val args = s"routeId=$routeId, changeSetId$changeSetId"
    api.execute(user, "long-distance-route-change", args) {
      reply(longDistanceRouteChangePageBuilder.build(routeId, changeSetId))
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }

}
