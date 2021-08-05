package kpn.server.analyzer.load.orphan.route

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RouteElements
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.OldRouteLoader
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class OrphanRoutesLoaderWorkerImpl(
  analysisContext: AnalysisContext,
  routeLoader: OldRouteLoader,
  routeRepository: RouteRepository,
  routeAnalyzer: MasterRouteAnalyzer,
  relationAnalyzer: RelationAnalyzer,
  nodeRepository: NodeRepository,
  networkNodeAnalyzer: NetworkNodeAnalyzer,
  nodeAnalyzer: NodeAnalyzer
) extends OrphanRoutesLoaderWorker {

  private val log = Log(classOf[OrphanRoutesLoaderWorkerImpl])

  override def process(timestamp: Timestamp, routeId: Long): Unit = {
    log.infoElapsed {
      val loadedRouteOption = routeLoader.loadRoute(timestamp, routeId)
      loadedRouteOption match {
        case Some(loadedRoute) =>

          val analysis = routeAnalyzer.analyze(loadedRoute.relation /*, orphan = true*/).get
          val route = analysis.route.copy(/*orphan = true*/)
          routeRepository.save(route)
          routeRepository.saveElements(
            RouteElements(
              loadedRoute.id,
              loadedRoute.id,
              relationAnalyzer.toElementIds(analysis.relation)
            )
          )

          val allNodes = networkNodeAnalyzer.analyze(loadedRoute.scopedNetworkType, loadedRoute.data)

          allNodes.values.foreach { networkNode =>
            val nodeAnalysis = nodeAnalyzer.analyze(NodeAnalysis(networkNode.node.raw))
            nodeRepository.save(nodeAnalysis.toNodeInfo)
          }

          val elementIds = relationAnalyzer.toElementIds(loadedRoute.relation)
          analysisContext.data.orphanRoutes.watched.add(loadedRoute.id, elementIds)

        case None => // error already logged in routeLoader
      }
      (s"""Loaded route $routeId""", ())
    }
  }
}
