package kpn.server.analyzer.engine.analysis.network

import kpn.core.analysis.NetworkNode
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.server.analyzer.load.data.LoadedRoute
import org.springframework.stereotype.Component

@Component
class NetworkRouteAnalyzerImpl(
  analysisContext: AnalysisContext,
  countryAnalyzer: CountryAnalyzer,
  relationAnalyzer: RelationAnalyzer,
  routeAnalyzer: MasterRouteAnalyzer
) extends NetworkRouteAnalyzer {

  private val log = Log(classOf[NetworkRouteAnalyzerImpl])

  override def analyze(networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork): Map[Long, RouteAnalysis] = {

    val routeRelations = loadedNetwork.data.relations.values.filter { rel =>
      analysisContext.isReferencedRouteRelation(loadedNetwork.networkType, rel.raw)
    }

    val routeAnalyses = routeRelations.flatMap { routeRelation =>

      val country = countryAnalyzer.relationCountry(routeRelation) match {
        case None => networkRelationAnalysis.country
        case Some(e) => Some(e)
      }

      RelationAnalyzer.networkType(routeRelation.raw) match {
        case Some(routeNetworkType) =>
          if (loadedNetwork.networkType == routeNetworkType) {
            val name = relationAnalyzer.routeName(routeRelation)
            val loadedRoute = LoadedRoute(country, routeNetworkType, name, loadedNetwork.data, routeRelation)
            val routeAnalysis = routeAnalyzer.analyze(loadedRoute, orphan = false)
            Some(routeAnalysis)
          }
          else {
            val msg = s"Route networkType (${routeNetworkType.name}) does not match the network relation networkType ${loadedNetwork.name}."
            val programmingError = "This is an unexpected programming error."
            //noinspection SideEffectsInMonadicTransformation
            log.error(s"$msg $programmingError")
            None
          }

        case None =>
          val msg = s"Could not determing networkType in route relation."
          val programmingError = "This is an unexpected programming error."
          //noinspection SideEffectsInMonadicTransformation
          log.error(s"$msg $programmingError")
          None
      }
    }
    routeAnalyses.map(a => (a.route.id, a)).toMap
  }

}
