package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.location.Location
import kpn.server.analyzer.engine.analysis.location.RouteLocator
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

/*
  Calculates the route location (re-uses the previous route location (read from database) if the geometry
  of the route did not change (same geometry digest)).
*/
@Component
class RouteLocationAnalyzerImpl(routeRepository: RouteRepository, routeLocator: RouteLocator) extends RouteLocationAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    routeRepository.findRouteDetailById(context.relation.id) match {
      case Some(route) =>
        if (route.geometryDigest == context.geometryDigest) {
          context.copy(_locationAnalysis = Some(route.locationAnalysis))
        }
        else {
          locate(context)
        }

      case None =>
        locate(context)
    }
  }

  private def locate(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val routeLocationAnalysis = routeLocator.locate(context.segments)
    if (routeLocationAnalysis.location.isEmpty && context.country.nonEmpty) {
      val country = context.country.get.domain
      context.copy(
        _locationAnalysis = Some(
          RouteLocationAnalysis(
            location = Some(
              Location(Seq(country))
            ),
            candidates = Seq.empty,
            locationNames = Seq(country)
          )
        )
      )
    }
    else {
      context.copy(_locationAnalysis = Some(routeLocationAnalysis))
    }
  }
}
