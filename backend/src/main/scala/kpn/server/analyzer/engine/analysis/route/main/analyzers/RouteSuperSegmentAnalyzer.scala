package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class RouteSuperSegmentAnalyzer(routeRepository: RouteRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val superSegmentElementInfos = routeRepository.segments(context.routeIds)
    val superSegments = SuperSegmentBuilder.build(superSegmentElementInfos)
    context.copy(_superSegments = Some(superSegments))
  }
}
