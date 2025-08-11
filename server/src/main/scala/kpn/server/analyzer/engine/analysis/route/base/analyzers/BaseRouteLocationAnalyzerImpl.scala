package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.location.Location
import kpn.server.analyzer.engine.analysis.location.RouteLocator
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

/*
  Calculates the route location (re-uses the previous route location (read from database) if the geometry
  of the route did not change (same geometry digest)).
*/
@Component
class BaseRouteLocationAnalyzerImpl(
  routeRepository: RouteRepository,
  routeLocator: RouteLocator
) extends BaseRouteLocationAnalyzer {

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    routeRepository.findBaseRouteById(context.relation.id) match {
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

  private def locate(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    val routeLocationAnalysis = routeLocator.locate(context.analysisSegments)
    if (routeLocationAnalysis.location.isEmpty && context.countries.nonEmpty) {
      val countries = context.countries.map(_.toString)
      context.copy(
        _locationAnalysis = Some(
          RouteLocationAnalysis(
            location = Some(
              Location(countries)
            ),
            candidates = Seq.empty,
            locationNames = countries
          )
        )
      )
    }
    else {
      context.copy(_locationAnalysis = Some(routeLocationAnalysis))
    }
  }
}
