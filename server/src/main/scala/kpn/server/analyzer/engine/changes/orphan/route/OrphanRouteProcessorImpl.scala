package kpn.server.analyzer.engine.changes.orphan.route

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.NetworkNodeBuilder
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeInfoBuilder
import org.springframework.stereotype.Component

@Component
class OrphanRouteProcessorImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository,
  relationAnalyzer: RelationAnalyzer,
  countryAnalyzer: CountryAnalyzer,
  routeAnalyzer: MasterRouteAnalyzer
) extends OrphanRouteProcessor {

  private val log = Log(classOf[OrphanRouteProcessorImpl])

  def process(context: ChangeSetContext, loadedRoute: LoadedRoute): Option[RouteAnalysis] = {

    Log.context(s"route=${loadedRoute.id}") {
      log.elapsed {
        try {
          val allNodes = new NetworkNodeBuilder(analysisContext, loadedRoute.data, loadedRoute.networkType, countryAnalyzer).networkNodes
          val analysis = routeAnalyzer.analyze(allNodes, loadedRoute, orphan = true)
          val route = analysis.route.copy(orphan = true)
          analysisRepository.saveRoute(route)

          analysis.routeNodes.routeNodes.foreach { routeNode =>
            val country = countryAnalyzer.country(Seq(routeNode.node))
            val loadedNode = LoadedNode.from(country, routeNode.node.raw)
            val nodeInfo = NodeInfoBuilder.fromLoadedNode(loadedNode)
            analysisRepository.saveNode(nodeInfo)
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
