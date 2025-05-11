package kpn.server.analyzer.engine.changes.route

import kpn.api.common.Fact
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeCreateProcessor(
  analysisContext: AnalysisContext,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  routeRepository: RouteRepository,
  rawDataRepository: RawDataRepository,
) extends BaseRouteChangeSubProcessor {

  private val log = Log(classOf[BaseRouteChangeCreateProcessor])

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    rawDataRepository.route(changeSetContext.changeSet.timestampAfter, routeId) match {
      case None =>
        // TODO redesign report?
        println("route not found")
        changeSetContext
      case Some(rawRouteDoc) =>
        val baseRouteAnalysisContext = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.structure)
        if (!baseRouteAnalysisContext.abort) {
          analysisContext.watched.routes.add(routeId, baseRouteAnalysisContext.elementIds)
          val baseRouteDoc = new BaseRouteDocBuilder(baseRouteAnalysisContext).build()
          routeRepository.saveBaseRoute(baseRouteDoc)
          changeSetContext.withImpact(
            tiles = baseRouteDoc.tiles,
            nodeIds = baseRouteDoc.nodes.nodeIds,
            routeIds = Seq(routeId),
          )
        }
        else {
          if (baseRouteAnalysisContext.facts.contains(Fact.LostRouteTags)) {
            val baseRouteDoc = new BaseRouteDocBuilder(baseRouteAnalysisContext).build() // TODO redesign - this is not going to work because info is incomplete???
            changeSetContext.withImpact(
              tiles = baseRouteDoc.tiles,
              nodeIds = baseRouteDoc.nodes.nodeIds,
              routeIds = Seq(routeId),
            )
          }
          else {
            changeSetContext
          }
        }
    }
  }
}
