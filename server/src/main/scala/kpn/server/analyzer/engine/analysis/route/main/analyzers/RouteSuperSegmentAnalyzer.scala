package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteSuperSegmentAnalyzer(routeRepository: RouteRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val superSegmentElementInfos = routeRepository.segments(context.routeIds)
    val superSegments = SuperSegmentBuilder.build(superSegmentElementInfos)
    context.copy(_superSegments = Some(superSegments))
  }
}
