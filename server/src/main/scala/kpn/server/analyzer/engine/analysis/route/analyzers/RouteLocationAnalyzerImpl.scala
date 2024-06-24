package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.location.Location
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
    routeRepository.findById(context.relation.id) match {
      case Some(route) =>
        if (route.analysis.geometryDigest == context.geometryDigest) {
          context.copy(locationAnalysis = Some(route.analysis.locationAnalysis))
        }
        else {
          locate(context)
        }

      case None =>
        locate(context)
    }
  }

  private def locate(context: RouteAnalysisContext): RouteAnalysisContext = {
    val routeLocationAnalysis = routeLocator.locate(context.routeMap)
    if (routeLocationAnalysis.location.isEmpty && context.country.nonEmpty) {
      val country = context.country.get.domain
      context.copy(
        locationAnalysis = Some(
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
      context.copy(locationAnalysis = Some(routeLocationAnalysis))
    }
  }
}
