package kpn.server.api.analysis.pages.route

import kpn.api.common.common.Ref
import kpn.api.common.route.MapRouteDetail
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MapRouteDetailBuilderImpl(
  routeRepository: RouteRepository
) extends MapRouteDetailBuilder {
  def build(routeId: Long): Option[MapRouteDetail] = {
    routeRepository.findById(routeId).map { route =>
      val networkReferences = buildNetworkReferences(routeId)
      MapRouteDetail(
        routeId,
        route.summary.name,
        networkReferences
      )
    }
  }

  private def buildNetworkReferences(routeId: Long): Seq[Ref] = {
    routeRepository.networkReferences(routeId).map(r => Ref(r.id, r.name))
  }
}
