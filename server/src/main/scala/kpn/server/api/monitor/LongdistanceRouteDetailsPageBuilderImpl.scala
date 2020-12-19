package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteDetailsPage
import kpn.server.repository.LongdistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongdistanceRouteDetailsPageBuilderImpl(
  longdistanceRouteRepository: LongdistanceRouteRepository
) extends LongdistanceRouteDetailsPageBuilder {

  override def build(routeId: Long): Option[LongdistanceRouteDetailsPage] = {
    longdistanceRouteRepository.routeWithId(routeId).map { route =>
      LongdistanceRouteDetailsPage(
        route.id,
        route.ref,
        route.name,
        route.nameNl,
        route.nameEn,
        route.nameDe,
        route.nameFr,
        route.description,
        route.operator,
        route.website,
        route.wayCount,
        route.osmDistance,
        route.gpxDistance,
        route.gpxFilename,
        route.gpxFilename.isDefined && route.osmSegments.size == 1 && route.nokSegments.isEmpty,
        route.osmSegments.size,
        route.nokSegments.size
      )
    }
  }
}
