package kpn.server.analyzer.engine.analysis.route.analyzers.route

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteDetailRepository
import org.springframework.stereotype.Component

@Component
class RouteBoundsAnalyzer(routeDetailRepository: RouteDetailRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val bounds = routeDetailRepository.bounds(context.routeIds)
    context.copy(_bounds = Some(bounds))
  }
}
