package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteMapPage
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceRouteMapPageBuilderImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository
) extends LongDistanceRouteMapPageBuilder {

  override def build(routeId: Long): Option[LongDistanceRouteMapPage] = {
    longDistanceRouteRepository.routeWithId(routeId).map { route =>
      LongDistanceRouteMapPage(
        route.id,
        route.ref,
        route.name,
        route.nameNl,
        route.nameEn,
        route.nameDe,
        route.nameFr,
        route.bounds,
        route.gpxFilename,
        route.osmSegments,
        route.gpxGeometry,
        route.okGeometry,
        route.nokSegments
      )
    }
  }

}
