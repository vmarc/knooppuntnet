package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.route.ParentRoute
import kpn.core.doc.ParentRouteData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class RouteParentAnalyzer(routeRepository: RouteRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val parentRoutes = findParentRoutes(context.route._id, 1, Seq.empty)
    context.copy(
      _parentRoutes = Some(parentRoutes)
    )
  }

  private def findParentRoutes(routeId: Long, level: Int, visitedRouteIds: Seq[Long]): Seq[ParentRoute] = {
    if (visitedRouteIds.contains(routeId)) {
      return Seq.empty
    }
    val directParentRoutes = routeRepository.parentRoutes(routeId).map(newParentRoute(level, _))
    val grandParentRoutes = directParentRoutes.flatMap { parentRoute =>
      findParentRoutes(parentRoute.routeId, level + 1, visitedRouteIds :+ routeId)
    }
    directParentRoutes ++ grandParentRoutes
  }

  private def newParentRoute(level: Int, data: ParentRouteData): ParentRoute = {
    ParentRoute(level, data.routeId, data.name)
  }
}
