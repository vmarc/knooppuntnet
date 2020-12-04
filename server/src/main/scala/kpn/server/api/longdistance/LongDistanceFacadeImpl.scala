package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRoute
import kpn.api.custom.ApiResponse
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceFacadeImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository
) extends LongDistanceFacade {

  override def route(routeId: Long): ApiResponse[LongDistanceRoute] = {
    ApiResponse(null, 1, longDistanceRouteRepository.routeWithId(routeId))
  }

  override def routes(): ApiResponse[Seq[LongDistanceRoute]] = {
    val routes = longDistanceRouteRepository.all()
    val sortedRoutes = routes.sortWith { (a, b) =>
      val ref1 = a.ref.getOrElse("zz")
      val ref2 = b.ref.getOrElse("zz")
      if (ref1 == ref2) {
        a.name < b.name
      }
      else {
        ref1 < ref2
      }
    }

    ApiResponse(null, 1, Some(sortedRoutes))
  }

}
