package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.network.Integrity
import kpn.core.util.Formatter.percentage
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkInfoIntegrityAnalyzer extends NetworkInfoAnalyzer {
  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkInfoIntegrityAnalyzer(context).analyze()
  }
}

class NetworkInfoIntegrityAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze(): NetworkInfoAnalysisContext = {

    val networkNodeIntegrities = collectNodeIntegrities()

    val isOk = if (networkNodeIntegrities.isEmpty) true else networkNodeIntegrities.forall(_.ok)
    val hasChecks = networkNodeIntegrities.nonEmpty
    val count = if (networkNodeIntegrities.isEmpty) "-" else networkNodeIntegrities.size.toString
    val okCount = networkNodeIntegrities.count(_.ok)
    val nokCount = networkNodeIntegrities.count(_.notOk)
    val coverage = percentage(networkNodeIntegrities.size, context.nodeDocs.size)
    val okRate = percentage(okCount, networkNodeIntegrities.size)
    val nokRate = percentage(nokCount, networkNodeIntegrities.size)

    context.copy(
      integrity = Integrity(
        isOk,
        hasChecks,
        count,
        okCount,
        nokCount,
        coverage,
        okRate,
        nokRate
      )
    )
  }

  private def collectNodeIntegrities(): Seq[NetworkNodeIntegrity] = {
    context.nodeDocs.flatMap { nodeDoc =>
      nodeDoc.nodeIntegrityDetail(context.scopedNetworkType).map { nodeIntegrityDetail =>
        NetworkNodeIntegrity(
          nodeDoc._id,
          nodeIntegrityDetail.expectedRouteCount,
          nodeIntegrityDetail.routeRefs.size
        )
      }
    }
  }
}
