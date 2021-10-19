package kpn.server.analyzer.engine.analysis.network.info

import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Fact
import kpn.core.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

class NetworkInfoDocBuilder(context: NetworkInfoAnalysisContext) {

  def build(): NetworkInfoDoc = {

    val summary = buildSummary()
    val detail = buildDetail()

    val facts = Fact.all.flatMap { fact => // use fact sorting order as defined in Fact class
      context.networkFacts.filter(_.name == fact.name)
    }

    NetworkInfoDoc(
      context.networkDoc._id,
      context.networkDoc.active,
      context.country,
      summary,
      detail,
      facts,
      context.nodeDetails,
      context.routeDetails,
      context.extraNodeIds,
      context.extraWayIds,
      context.extraRelationIds
    )
  }

  private def buildSummary(): NetworkSummary = {
    NetworkSummary(
      context.name,
      context.scopedNetworkType.networkType,
      context.scopedNetworkType.networkScope,
      context.networkFacts.map(_.size).sum + context.facts.size,
      context.nodeDetails.size,
      context.routeDetails.size,
      context.changeCount
    )
  }

  private def buildDetail(): NetworkDetail = {
    NetworkDetail(
      context.km,
      context.meters,
      context.lastUpdated.get,
      context.networkDoc.relationLastUpdated,
      context.lastSurvey,
      context.networkDoc.tags,
      context.brokenRouteCount,
      context.brokenRoutePercentage,
      context.integrity,
      context.inaccessibleRouteCount,
      context.connectionCount,
      context.center
    )
  }
}
