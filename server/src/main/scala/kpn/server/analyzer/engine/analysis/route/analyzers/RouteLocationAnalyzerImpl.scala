package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.location.RouteLocator
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

/*
  Calculates the route location (re-uses the previous route location (read from database) if the geometry
  of the route did not change (same geometry digest)).
*/
@Component
class RouteLocationAnalyzerImpl(routeRepository: RouteRepository, routeLocator: RouteLocator) extends RouteLocationAnalyzer {

  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {

    context.geometryDigest match {
      case None => throw new IllegalStateException("geometryDigest not known (route analyzers in wrong order?)")
      case Some(geometryDigest) =>
        context.routeMap match {
          case None => throw new IllegalStateException("routeMap not known (route analyzers in wrong order?)")
          case Some(routeMap) =>
            routeRepository.findById(context.relation.id) match {
              case Some(route) =>
                if (route.analysis.geometryDigest == geometryDigest) {
                  context.copy(locationAnalysis = Some(route.analysis.locationAnalysis))
                }
                else {
                  val routeLocationAnalysis = routeLocator.locate(routeMap)
                  context.copy(locationAnalysis = Some(routeLocationAnalysis))
                }

              case None =>
                val routeLocationAnalysis = routeLocator.locate(routeMap)
                context.copy(locationAnalysis = Some(routeLocationAnalysis))
            }
        }
    }
  }
}
