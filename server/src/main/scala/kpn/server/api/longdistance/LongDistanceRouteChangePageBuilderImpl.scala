package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangePage
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceRouteChangePageBuilderImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository
) extends LongDistanceRouteChangePageBuilder {

  override def build(routeId: Long, changeId: Long): Option[LongDistanceRouteChangePage] = {

    longDistanceRouteRepository.change(routeId, changeId).flatMap { change =>
      longDistanceRouteRepository.routeWithId(routeId).map { route =>
        LongDistanceRouteChangePage(
          route.id,
          route.ref,
          route.name,
          change
        )
      }
    }
  }

}
