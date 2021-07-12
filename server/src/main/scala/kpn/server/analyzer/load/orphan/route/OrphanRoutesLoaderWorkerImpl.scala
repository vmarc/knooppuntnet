package kpn.server.analyzer.load.orphan.route

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RouteElements
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.RouteLoader
import kpn.server.repository.NodeInfoBuilder
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class OrphanRoutesLoaderWorkerImpl(
  analysisContext: AnalysisContext,
  routeLoader: RouteLoader,
  routeRepository: RouteRepository,
  routeAnalyzer: MasterRouteAnalyzer,
  relationAnalyzer: RelationAnalyzer,
  nodeRepository: NodeRepository,
  nodeInfoBuilder: NodeInfoBuilder,
  networkNodeAnalyzer: NetworkNodeAnalyzer
) extends OrphanRoutesLoaderWorker {

  private val log = Log(classOf[OrphanRoutesLoaderWorkerImpl])

  override def process(timestamp: Timestamp, routeId: Long): Unit = {
    log.unitElapsed {
      val loadedRouteOption = routeLoader.loadRoute(timestamp, routeId)
      loadedRouteOption match {
        case Some(loadedRoute) =>

          val analysis = routeAnalyzer.analyze(loadedRoute, orphan = true)
          val route = analysis.route.copy(orphan = true)
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
            nodeRepository.save(
              nodeInfoBuilder.build(
                id = networkNode.id,
                active = true,
                orphan = false,
                country = networkNode.country,
                latitude = networkNode.node.latitude,
                longitude = networkNode.node.longitude,
                lastUpdated = networkNode.node.timestamp,
                tags = networkNode.node.tags,
                facts = Seq.empty
              )
            )
          }

          val elementIds = relationAnalyzer.toElementIds(loadedRoute.relation)
          analysisContext.data.orphanRoutes.watched.add(loadedRoute.id, elementIds)

        case None => // error already logged in routeLoader
      }
      s"""Loaded route $routeId"""
    }
  }
}
