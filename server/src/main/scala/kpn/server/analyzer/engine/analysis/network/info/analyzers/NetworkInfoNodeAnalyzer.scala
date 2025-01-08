package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.util.NaturalSorting
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkInfoNodeAnalyzer extends NetworkInfoAnalyzer {
  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkInfoNodeAnalyzer(context).analyze()
  }
}

class NetworkInfoNodeAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze(): NetworkInfoAnalysisContext = {
    val nodeDetails = analyzeNetworkNodes()
    context.copy(
      nodeDetails = nodeDetails
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
