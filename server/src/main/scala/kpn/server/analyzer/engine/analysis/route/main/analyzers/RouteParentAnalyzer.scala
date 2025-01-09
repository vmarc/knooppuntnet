package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.route.ParentRoute
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteDetailRepository
import org.springframework.stereotype.Component

@Component
class RouteParentAnalyzer(routeDetailRepository: RouteDetailRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val parentRoutes = findParentRoutes(context.routeDetailDoc.id, 1, Seq.empty)
    context.copy(
      _parentRoutes = Some(parentRoutes)
    )
  }

  private def findParentRoutes(routeId: Long, level: Int, visitedRouteIds: Seq[Long]): Seq[ParentRoute] = {
    if (visitedRouteIds.contains(routeId)) {
      Seq.empty
    }
    else {
      val routes = routeDetailRepository.parentRoutes(routeId).map(data => ParentRoute(level, data.routeId, data.name))
      val grandParentRoutes = routes.flatMap { parentRoute =>
        findParentRoutes(parentRoute.routeId, level + 1, visitedRouteIds :+ routeId)
      }
      routes ++ grandParentRoutes
    }
  }
}
