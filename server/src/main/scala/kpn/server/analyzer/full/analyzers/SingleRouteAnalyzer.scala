package kpn.server.analyzer.full.analyzers

import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class SingleRouteAnalyzer(
  routeRepository: RouteRepository,
  routeMainAnalyzer: RouteMainAnalyzer,
) {
  def processRoute(routeId: Long): Unit = {
    routeRepository.findBaseRouteById(routeId).foreach { baseRouteDoc =>
      routeMainAnalyzer.analyze(baseRouteDoc).foreach { routeDoc =>
        routeRepository.saveRoute(routeDoc)
      }
    }
  }
}
