package kpn.server.api.monitor.longdistance

import kpn.api.common.monitor.LongdistanceRouteDetail
import kpn.api.common.monitor.LongdistanceRoutesPage
import kpn.server.repository.LongdistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongdistanceRoutesPageBuilderImpl(
  longdistanceRouteRepository: LongdistanceRouteRepository
) extends LongdistanceRoutesPageBuilder {

  override def build(): Option[LongdistanceRoutesPage] = {

    val routes = longdistanceRouteRepository.all()
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
      LongdistanceRouteDetail(
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
      LongdistanceRoutesPage(
        details
      )
    )
  }

}
