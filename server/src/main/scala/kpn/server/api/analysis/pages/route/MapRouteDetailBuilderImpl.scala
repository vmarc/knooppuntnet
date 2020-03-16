package kpn.server.api.analysis.pages.route

import kpn.api.common.common.Ref
import kpn.api.common.route.MapRouteDetail
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MapRouteDetailBuilderImpl(
  routeRepository: RouteRepository
) extends MapRouteDetailBuilder {
  def build(user: Option[String], routeId: Long): Option[MapRouteDetail] = {
    routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
      val networkReferences = buildNetworkReferences(routeId)
      MapRouteDetail(
        routeId,
        route.summary.name,
        networkReferences
      )
    }
  }

  private def buildNetworkReferences(routeId: Long): Seq[Ref] = {
    routeRepository.routeReferences(routeId, Couch.uiTimeout).networkReferences.map(r => Ref(r.id, r.name))
  }

}
