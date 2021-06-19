package kpn.server.analyzer.engine.changes.orphan.route

import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RouteElements
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.server.repository.NodeInfoBuilder
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class OrphanRouteProcessorImpl(
  analysisContext: AnalysisContext,
  nodeRepository: NodeRepository,
  relationAnalyzer: RelationAnalyzer,
  countryAnalyzer: CountryAnalyzer,
  routeRepository: RouteRepository,
  routeAnalyzer: MasterRouteAnalyzer,
  nodeInfoBuilder: NodeInfoBuilder,
  nodeAnalyzer: NodeAnalyzer
) extends OrphanRouteProcessor {

  private val log = Log(classOf[OrphanRouteProcessorImpl])

  def process(context: ChangeSetContext, loadedRoute: LoadedRoute): Option[RouteAnalysis] = {

    Log.context(s"route=${loadedRoute.id}") {
      log.elapsed {
        try {
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
          analysis.routeNodeAnalysis.routeNodes.foreach { routeNode =>
            val country = countryAnalyzer.country(Seq(routeNode.node))
            val networkTypes = NetworkType.all.filter { networkType =>
              analysisContext.isValidNetworkNode(networkType, routeNode.node.raw)
            }
            val name = nodeAnalyzer.name(routeNode.node.tags)
            val loadedNode = LoadedNode(country, networkTypes, name, routeNode.node)
            val nodeInfo = nodeInfoBuilder.fromLoadedNode(loadedNode)
            nodeRepository.save(nodeInfo)
          }

          val elementIds = relationAnalyzer.toElementIds(loadedRoute.relation)
          analysisContext.data.orphanRoutes.watched.add(loadedRoute.id, elementIds)
          ("orphan route analysis", Some(analysis))
        }
        catch {
          case e: Throwable =>
            log.error("problem building route", e)
            throw e
        }
      }
    }
  }
}
