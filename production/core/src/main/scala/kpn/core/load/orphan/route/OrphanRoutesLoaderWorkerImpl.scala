package kpn.core.load.orphan.route

import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.NetworkNodeBuilder
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzer
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.RouteLoader
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.NodeInfoBuilder
import kpn.core.util.Log
import kpn.shared.Timestamp

class OrphanRoutesLoaderWorkerImpl(
  routeLoader: RouteLoader,
  routeAnalyzer: MasterRouteAnalyzer,
  countryAnalyzer: CountryAnalyzer,
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository
) extends OrphanRoutesLoaderWorker {

  val log = Log(classOf[OrphanRoutesLoaderWorkerImpl])

  override def process(timestamp: Timestamp, routeId: Long): Unit = {
    log.unitElapsed {
      val loadedRouteOption = routeLoader.loadRoute(timestamp, routeId)
      loadedRouteOption match {
        case Some(loadedRoute) =>

          val allNodes = new NetworkNodeBuilder(loadedRoute.data, countryAnalyzer).networkNodes
          val analysis = routeAnalyzer.analyze(allNodes, loadedRoute, orphan = true)
          val route = analysis.route.copy(orphan = true)
          analysisRepository.saveRoute(route)

          allNodes.values.foreach { networkNode =>
            analysisRepository.saveNode(
              NodeInfoBuilder.build(
                id = networkNode.id,
                active = true,
                display = true,
                ignored = false,
                orphan = false,
                country = networkNode.country,
                latitude = networkNode.node.latitude,
                longitude = networkNode.node.longitude,
                lastUpdated = networkNode.node.timestamp,
                tags = networkNode.node.tags,
                facts = Seq()
              )
            )
          }

          val elementIds = RelationAnalyzer.toElementIds(loadedRoute.relation)
          analysisData.orphanRoutes.watched.add(loadedRoute.id, elementIds)

        case None => // error already logged in routeLoader
      }
      s"""Loaded route $routeId"""
    }
  }
}
