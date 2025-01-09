package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.BaseRouteRepository
import org.springframework.stereotype.Component

@Component
class RouteBoundsAnalyzer(baseRouteRepository: BaseRouteRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val bounds = baseRouteRepository.bounds(context.routeIds)
    context.copy(_bounds = Some(bounds))
  }
}
