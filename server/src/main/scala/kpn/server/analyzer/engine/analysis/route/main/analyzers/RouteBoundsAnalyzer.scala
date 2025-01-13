package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteBoundsAnalyzer(routeRepository: RouteRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val bounds = routeRepository.bounds(context.routeIds)
    context.copy(_bounds = Some(bounds))
  }
}
