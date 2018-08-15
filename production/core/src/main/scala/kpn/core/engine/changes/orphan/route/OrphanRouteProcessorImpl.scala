package kpn.core.engine.changes.orphan.route

import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.NetworkNodeBuilder
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.engine.analysis.route.MasterRouteAnalyzer
import kpn.core.load.data.LoadedRoute
import kpn.core.repository.AnalysisRepository
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.data.LoadedNode
import kpn.core.repository.NodeInfoBuilder
import kpn.core.util.Log

class OrphanRouteProcessorImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  countryAnalyzer: CountryAnalyzer,
  routeAnalyzer: MasterRouteAnalyzer
) extends OrphanRouteProcessor {

  private val log = Log(classOf[OrphanRouteProcessorImpl])

  def process(context: ChangeSetContext, loadedRoute: LoadedRoute): Option[RouteAnalysis] = {

    Log.context(s"route=${loadedRoute.id}") {
      log.elapsed {
        try {
          val allNodes = new NetworkNodeBuilder(loadedRoute.data, countryAnalyzer).networkNodes
          val analysis = routeAnalyzer.analyze(allNodes, loadedRoute, orphan = true)
          val route = analysis.route.copy(orphan = true)
          analysisRepository.saveRoute(route)

          analysis.routeNodes.routeNodes.foreach { routeNode =>
            val country = countryAnalyzer.country(Seq(routeNode.node))
            val loadedNode = LoadedNode.from(country, routeNode.node.raw)
            val nodeInfo = NodeInfoBuilder.fromLoadedNode(loadedNode)
            analysisRepository.saveNode(nodeInfo)
          }

          val elementIds = RelationAnalyzer.toElementIds(loadedRoute.relation)
          if (analysis.route.ignored) {
            analysisData.orphanRoutes.ignored.add(loadedRoute.id, elementIds)
            ("orphan route analysis", None)
          }
          else {
            analysisData.orphanRoutes.watched.add(loadedRoute.id, elementIds)
            ("orphan route analysis", Some(analysis))
          }
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
