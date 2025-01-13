package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.route.ParentRoute
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteParentAnalyzer(routeRepository: RouteRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val parentRoutes = findParentRoutes(context.route.id, 1, Seq.empty)
    context.copy(
      _parentRoutes = Some(parentRoutes)
    )
  }

  private def findParentRoutes(routeId: Long, level: Int, visitedRouteIds: Seq[Long]): Seq[ParentRoute] = {
    if (visitedRouteIds.contains(routeId)) {
      Seq.empty
    }
    else {
      val routes = routeRepository.parentRoutes(routeId).map(data => ParentRoute(level, data.routeId, data.name))
      val grandParentRoutes = routes.flatMap { parentRoute =>
        findParentRoutes(parentRoute.routeId, level + 1, visitedRouteIds :+ routeId)
      }
      routes ++ grandParentRoutes
    }
  }
}
