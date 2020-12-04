package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangesPage
import kpn.api.common.longdistance.LongDistanceRouteDetail
import kpn.api.common.longdistance.LongDistanceRouteDetailsPage
import kpn.api.common.longdistance.LongDistanceRouteMapPage
import kpn.api.common.longdistance.LongDistanceRoutesPage
import kpn.api.custom.ApiResponse
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceFacadeImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository
) extends LongDistanceFacade {

  override def routes(): ApiResponse[LongDistanceRoutesPage] = {
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
        route.gpxFilename
      )
    }

    ApiResponse(
      null,
      1,
      Some(
        LongDistanceRoutesPage(
          details
        )
      )
    )
  }

  override def route(routeId: Long): ApiResponse[LongDistanceRouteDetailsPage] = {
    ApiResponse(
      null,
      1,
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
          route.gpxFilename
        )
      }
    )
  }

  override def routeMap(routeId: Long): ApiResponse[LongDistanceRouteMapPage] = {
    ApiResponse(
      null,
      1,
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
          route.osmGeometry,
          route.gpxGeometry,
          route.okGeometry,
          route.nokGeometry
        )
      }
    )
  }

  override def routeChanges(routeId: Long): ApiResponse[LongDistanceRouteChangesPage] = {
    ApiResponse(
      null,
      1,
      longDistanceRouteRepository.routeWithId(routeId).map { route =>
        LongDistanceRouteChangesPage(
          route.id,
          route.ref,
          route.name
        )
      }
    )
  }
}
