package kpn.core.load.orphan.route

import kpn.core.engine.analysis.NetworkNodeBuilder
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzer
import kpn.core.load.RouteLoader
import kpn.core.repository.AnalysisRepository
import kpn.core.util.Log
import kpn.shared.Timestamp

class OrphanRoutesLoaderWorkerImpl(
  routeLoader: RouteLoader,
  routeAnalyzer: MasterRouteAnalyzer,
  countryAnalyzer: CountryAnalyzer,
  analysisRepository: AnalysisRepository
) extends OrphanRoutesLoaderWorker {

  val log = Log(classOf[OrphanRoutesLoaderWorkerImpl])

  def process(timestamp: Timestamp, routeId: Long): Unit = {
    log.unitElapsed {
      val loadedRouteOption = routeLoader.loadRoute(timestamp, routeId)
      loadedRouteOption match {
        case Some(loadedRoute) =>

          val allNodes = new NetworkNodeBuilder(loadedRoute.data, countryAnalyzer).networkNodes
          val analysis = routeAnalyzer.analyze(allNodes, loadedRoute, orphan = true)
          val route = analysis.route.copy(orphan = true)
          analysisRepository.saveRoute(route)

        // TODO CHANGE should update AnalyzerData?
        // TODO CHANGE should do ignored route analysis?

        case None => // error already logged in routeLoader
      }
      s"Loaded route $routeId"
    }
  }
}
