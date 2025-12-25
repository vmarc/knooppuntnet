package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.util.NaturalSorting

object NetworkNodeAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkNodeAnalyzer(context).analyze()
  }
}

class NetworkNodeAnalyzer(context: NetworkAnalysisContext) {

  def analyze(): NetworkAnalysisContext = {
    val nodeDetails = if (context.network.active) {
      analyzeNetworkNodes()
    }
    else {
      Seq.empty
    }
    context.copy(
      _nodeDetails = Some(nodeDetails)
    )
  }

  private def analyzeNetworkNodes(): Seq[NetworkInfoNodeDetail] = {
    val sortedNodeDocs = NaturalSorting.sortBy(context.nodeDocs)(_.name(context.scopedRouteType))
    sortedNodeDocs.map { nodeDoc =>
      val networkDocAnalyzer = new NetworkDocAnalyzer(context, nodeDoc)
      NetworkInfoNodeDetail(
        nodeDoc._id,
        nodeDoc.name(context.scopedRouteType),
        networkDocAnalyzer.longName,
        nodeDoc.base.latitude,
        nodeDoc.base.longitude,
        networkDocAnalyzer.connection,
        networkDocAnalyzer.roleConnection,
        networkDocAnalyzer.definedInRelation,
        networkDocAnalyzer.proposed,
        nodeDoc.base.raw.timestamp,
        nodeDoc.base.lastSurvey,
        networkDocAnalyzer.expectedRouteCount,
        nodeDoc.facts
      )
    }
  }
}
