package kpn.server.analyzer.engine.analysis.network.main

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.network.NetworkDetail
import kpn.api.custom.Timestamp
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkAnalysisContext
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCenterAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkFactAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkIntegrityAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkLastUpdatedAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkNameAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkProposedAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkRouteAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkTagAnalyzer
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
@Profile(Array("analysis"))
class NetworkMainAnalyzer(
  database: Database,
  networkRouteAnalyzer: NetworkRouteAnalyzer,
  networkNodeDocAnalyzer: NetworkNodeDocAnalyzer,
  networkCountryAnalyzer: NetworkCountryAnalyzer,
  networkExtraAnalyzer: NetworkExtraAnalyzer
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
        NetworkTagAnalyzer,
        NetworkProposedAnalyzer,
        networkRouteAnalyzer,
        networkNodeDocAnalyzer,
        NetworkNodeAnalyzer,
        NetworkIntegrityAnalyzer,
        NetworkFactAnalyzer,
        networkCountryAnalyzer,
        networkExtraAnalyzer,
        NetworkCenterAnalyzer,
        NetworkLastUpdatedAnalyzer
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[NetworkAnalyzer], context: NetworkAnalysisContext): Option[NetworkDoc] = {
    if (analyzers.isEmpty) {
      val factCount = if (context.network.active) {
        context.networkFacts.map(_.size).sum + context.facts.size
      }
      else {
        0
      }

      val detail = buildDetail(context)
      val facts = Fact.values.flatMap { fact => // use fact sorting order as defined in Fact class
        context.networkFacts.filter(_.fact == fact)
      }

      val base = if (context.network.active) {
        context.network.base
      } else {
        context.network.base.copy(members = Seq.empty)
      }

      val networkNodeIds = context.nodeDetails.filterNot(isConnection).map(_.id)
      val connectionNodeIds = context.nodeDetails.filter(isConnection).map(_.id)
      val networkRouteIds = context.routeDetails.filterNot(_.roleConnection).map(_.id)
      val connectionRouteIds = context.routeDetails.filter(_.roleConnection).map(_.id)

      Some(
        NetworkDoc(
          _id = context.network._id,
          active = context.network.active,
          base = base,
          country = context.country,
          detail = detail,
          facts = facts,
          nodes = context.nodeDetails,
          routes = context.routeDetails,
          factCount = factCount,
          nodeCount = context.nodeDetails.size,
          routeCount = context.routeDetails.size,
          extraNodeIds = context.extraNodeIds,
          extraWayIds = context.extraWayIds,
          extraRelationIds = context.extraRelationIds,
          networkNodeIds = networkNodeIds,
          connectionNodeIds = connectionNodeIds,
          networkRouteIds = networkRouteIds,
          connectionRouteIds = connectionRouteIds,
          stamp = None,
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }

  private def buildDetail(context: NetworkAnalysisContext): NetworkDetail = {
    val networkNodeInfos = context.nodeDetails.filter(node => node.definedInRelation)
    val bounds = Option.when(networkNodeInfos.nonEmpty) {
      Bounds.from(networkNodeInfos)
    }
    NetworkDetail(
      context.km,
      context.meters,
      context.lastUpdated.get,
      context.network.base.raw.timestamp,
      context.lastSurvey,
      context.brokenRouteCount,
      context.brokenRoutePercentage,
      context.integrity,
      context.inaccessibleRouteCount,
      context.connectionCount,
      bounds,
      context.center
    )
  }

  private def isConnection(node: NetworkInfoNodeDetail): Boolean = {
    node.roleConnection || node.connection
  }
}
