package kpn.core.engine.changes.builder

import kpn.core.analysis.Network
import kpn.core.engine.analysis.NetworkNodeBuilder
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.engine.analysis.route.MasterRouteAnalyzer
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.load.RoutesLoader
import kpn.core.load.data.LoadedRoute
import kpn.core.tools.analyzer.AnalysisContext

class ChangeBuilderImpl(
  analysisContext: AnalysisContext,
  analysisData: AnalysisData,
  routesLoader: RoutesLoader,
  countryAnalyzer: CountryAnalyzer,
  routeAnalyzer: MasterRouteAnalyzer,
  routeChangeBuilder: RouteChangeBuilder,
  nodeChangeBuilder: NodeChangeBuilder
) extends ChangeBuilder {

  def build(context: ChangeSetContext, networkBefore: Option[Network], networkAfter: Option[Network]): ChangeSetChanges = {

    val changeBuilderContext = {

      val routeIdsBefore = networkBefore.toSeq.flatMap(_.routes.map(_.id)).toSet
      val routeIdsAfter = networkAfter.toSeq.flatMap(_.routes.map(_.id)).toSet

      val addedRouteIds = (routeIdsAfter -- routeIdsBefore).toSeq.sorted
      val removedRouteIds = (routeIdsBefore -- routeIdsAfter).toSeq.sorted

      val routeAnalysesBefore: Seq[RouteAnalysis] = collectRouteAnalysesBefore(context, networkBefore, addedRouteIds)
      val routeAnalysesAfter: Seq[RouteAnalysis] = collectRouteAnalysesAfter(context, networkAfter, removedRouteIds)

      ChangeBuilderContext(
        context,
        routeAnalysesBefore,
        routeAnalysesAfter,
        networkBefore,
        networkAfter
      )
    }

    val routeChanges = routeChangeBuilder.build(changeBuilderContext)
    val nodeChanges = nodeChangeBuilder.build(changeBuilderContext)

    ChangeSetChanges(routeChanges = routeChanges, nodeChanges = nodeChanges)
  }

  private def collectRouteAnalysesAfter(context: ChangeSetContext, networkAfter: Option[Network], removedRouteIds: Seq[Long]) = {
    val extraLoadedRoutesAfter: Seq[LoadedRoute] = routesLoader.load(context.timestampAfter, removedRouteIds).flatten
    val extraAnalysesAfter = extraLoadedRoutesAfter.map { loadedRoute =>
      val allNodes = new NetworkNodeBuilder(analysisContext, loadedRoute.data, countryAnalyzer).networkNodes
      routeAnalyzer.analyze(allNodes, loadedRoute, orphan = false)
    }
    networkAfter.toSeq.flatMap(_.routes.map(_.routeAnalysis)) ++ extraAnalysesAfter
  }

  private def collectRouteAnalysesBefore(context: ChangeSetContext, networkBefore: Option[Network], addedRouteIds: Seq[Long]) = {
    val extraLoadedRoutesBefore: Seq[LoadedRoute] = routesLoader.load(context.timestampBefore, addedRouteIds).flatten
    val extraAnalysesBefore = extraLoadedRoutesBefore.map { loadedRoute =>
      val orphan = analysisData.orphanRoutes.contains(loadedRoute.id)
      val allNodes = new NetworkNodeBuilder(analysisContext, loadedRoute.data, countryAnalyzer).networkNodes
      routeAnalyzer.analyze(allNodes, loadedRoute, orphan)
    }
    networkBefore.toSeq.flatMap(_.routes.map(_.routeAnalysis)) ++ extraAnalysesBefore
  }
}
