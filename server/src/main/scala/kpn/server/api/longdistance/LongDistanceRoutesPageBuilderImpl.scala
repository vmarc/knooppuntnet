package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteDetail
import kpn.api.common.longdistance.LongDistanceRoutesPage
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceRoutesPageBuilderImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository
) extends LongDistanceRoutesPageBuilder {

  override def build(): Option[LongDistanceRoutesPage] = {

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

    val details = sortedRoutes.map { route =>
      LongDistanceRouteDetail(
        route.id,
        route.ref,
        route.name,
        route.description,
        route.operator,
        route.website,
        route.wayCount,
        route.osmDistance,
        route.gpxDistance,
        route.gpxFilename,
        route.osmSegments.size == 1,
        route.gpxFilename.isDefined && route.nokSegments.isEmpty
      )
    }
    Some(
      LongDistanceRoutesPage(
        details
      )
    )
  }

}
