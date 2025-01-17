package kpn.server.analyzer.engine.analysis.network.main

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Timestamp
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkAnalysisContext
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCenterAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoFactAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoIntegrityAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoNodeAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoNodeMemberMissingAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoProposedAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoTagAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkLastUpdatedAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkNameAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkSurveyAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class NetworkMainAnalyzer(
  database: Database,
  networkInfoRouteAnalyzer: NetworkInfoRouteAnalyzer,
  networkInfoNodeDocAnalyzer: NetworkInfoNodeDocAnalyzer,
  networkCountryAnalyzer: NetworkCountryAnalyzer,
  networkInfoExtraAnalyzer: NetworkInfoExtraAnalyzer
) {

  def analyze(network: BaseNetworkDoc, analysisTimestamp: Timestamp, previousKnownCountry: Option[Country] = None): Option[NetworkDoc] = {
    Log.context(f"network=${network._id}%07d") {
      val context = NetworkAnalysisContext(
        network,
        analysisTimestamp,
        previousKnownCountry
      )
      val analyzers: List[NetworkAnalyzer] = List(
        NetworkSurveyAnalyzer,
        NetworkNameAnalyzer,
        NetworkInfoTagAnalyzer,
        NetworkInfoProposedAnalyzer,
        networkInfoRouteAnalyzer,
        networkInfoNodeDocAnalyzer,
        NetworkInfoNodeAnalyzer,
        NetworkInfoIntegrityAnalyzer,
        NetworkInfoFactAnalyzer,
        NetworkInfoNodeMemberMissingAnalyzer,
        networkCountryAnalyzer,
        networkInfoExtraAnalyzer,
        NetworkCenterAnalyzer,
        NetworkLastUpdatedAnalyzer
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[NetworkAnalyzer], context: NetworkAnalysisContext): Option[NetworkDoc] = {
    if (analyzers.isEmpty) {
      val summary = buildSummary(context)
      val detail = buildDetail(context)
      val facts = Fact.values.flatMap { fact => // use fact sorting order as defined in Fact class
        context.networkFacts.filter(_.fact == fact)
      }
      Some(
        NetworkDoc(
          context.network._id,
          context.network.active,
          context.country,
          summary,
          detail,
          facts,
          context.nodeDetails,
          context.routeDetails,
          context.extraNodeIds,
          context.extraWayIds,
          context.extraRelationIds,
          context.network.members,
          None
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }

  private def buildSummary(context: NetworkAnalysisContext): NetworkSummary = {
    val factCount = if (context.network.active) {
      context.networkFacts.map(_.size).sum + context.facts.size
    }
    else {
      0
    }
    NetworkSummary(
      context.name,
      context.scopedRouteType.routeType,
      context.scopedRouteType.routeScope,
      factCount,
      context.nodeDetails.size,
      context.routeDetails.size,
    )
  }

  private def buildDetail(context: NetworkAnalysisContext): NetworkDetail = {
    NetworkDetail(
      context.km,
      context.meters,
      context.network.version,
      context.network.changeSetId,
      context.lastUpdated.get,
      context.network.timestamp,
      context.lastSurvey,
      context.network.tags,
      context.brokenRouteCount,
      context.brokenRoutePercentage,
      context.integrity,
      context.inaccessibleRouteCount,
      context.connectionCount,
      context.center
    )
  }
}
