package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteDetailsPage
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceRouteDetailsPageBuilderImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository
) extends LongDistanceRouteDetailsPageBuilder {

  override def build(routeId: Long): Option[LongDistanceRouteDetailsPage] = {
    longDistanceRouteRepository.routeWithId(routeId).map { route =>
      LongDistanceRouteDetailsPage(
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
