package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.Relation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNetwork
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
      analysisContext.isReferencedRouteRelation(loadedNetwork.scopedNetworkType, rel.raw)
    }

    val routeAnalyses = routeRelations.flatMap { routeRelation =>
      try {
        analyzeRoute(networkRelationAnalysis, loadedNetwork, routeRelation)
      }
      catch {
        case e: Exception =>
          val message = s"Could not analyze route ${routeRelation.id}"
          log.error(message, e)
          throw new RuntimeException(message, e)
      }
    }
    routeAnalyses.map(a => (a.route.id, a)).toMap
  }

  private def analyzeRoute(networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork, routeRelation: Relation) = {
    val country = countryAnalyzer.relationCountry(routeRelation) match {
      case None => networkRelationAnalysis.country
      case Some(e) => Some(e)
    }

    RelationAnalyzer.scopedNetworkType(routeRelation.raw) match {
      case Some(routeScopedNetworkType) =>
        if (loadedNetwork.scopedNetworkType == routeScopedNetworkType) {
          val routeAnalysis = routeAnalyzer.analyze(routeRelation)
          Some(routeAnalysis)
        }
        else {
          val msg = s"Route scopedNetworkType (${routeScopedNetworkType.key}) does not match the network relation networkType ${loadedNetwork.name}."
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
}
