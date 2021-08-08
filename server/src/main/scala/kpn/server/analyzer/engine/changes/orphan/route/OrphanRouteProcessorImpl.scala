package kpn.server.analyzer.engine.changes.orphan.route

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RouteElements
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class OrphanRouteProcessorImpl(
  analysisContext: AnalysisContext,
  nodeRepository: NodeRepository,
  relationAnalyzer: RelationAnalyzer,
  routeRepository: RouteRepository,
  routeAnalyzer: MasterRouteAnalyzer,
  nodeAnalyzer: NodeAnalyzer
) extends OrphanRouteProcessor {

  private val log = Log(classOf[OrphanRouteProcessorImpl])

  def process(context: ChangeSetContext, loadedRoute: LoadedRoute): Option[RouteAnalysis] = {

    Log.context(s"route=${loadedRoute.id}") {
      log.infoElapsed {
        try {
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
          analysis.routeNodeAnalysis.routeNodes.foreach { routeNode =>
            val nodeAnalysis = nodeAnalyzer.analyze(NodeAnalysis(routeNode.node.raw))
            nodeRepository.save(nodeAnalysis.toNodeDoc)
          }

          val elementIds = relationAnalyzer.toElementIds(loadedRoute.relation)
          analysisContext.data.routes.watched.add(loadedRoute.id, elementIds)
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
