package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.util.NaturalSorting

object NetworkInfoNodeAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkInfoNodeAnalyzer(context).analyze()
  }
}

class NetworkInfoNodeAnalyzer(context: NetworkAnalysisContext) {

  def analyze(): NetworkAnalysisContext = {
    val nodeDetails = analyzeNetworkNodes()
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
        nodeDoc.latitude,
        nodeDoc.longitude,
        networkDocAnalyzer.connection,
        networkDocAnalyzer.roleConnection,
        networkDocAnalyzer.definedInRelation,
        networkDocAnalyzer.proposed,
        nodeDoc.lastUpdated,
        nodeDoc.lastSurvey,
        networkDocAnalyzer.expectedRouteCount,
        nodeDoc.facts
      )
    }
  }
}
