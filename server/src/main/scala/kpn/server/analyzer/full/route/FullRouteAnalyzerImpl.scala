package kpn.server.analyzer.full.route

import kpn.core.util.Log
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.analyzer.load.RouteLoader
import kpn.server.analyzer.load.orphan.route.RouteIdsLoader

class FullRouteAnalyzerImpl(
  routeIdsLoader: RouteIdsLoader
) extends FullRouteAnalyzer {

  private val log = Log(classOf[FullRouteAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {

    // val existingRouteIds = findActiveRouteIds()

    val allRouteIds = {
      val ids = routeIdsLoader.load(context.timestamp)
      log.info(s"${ids.size} route ids loaded")
      ids.take(175)
    }

    context
  }

}
