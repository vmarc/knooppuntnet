package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.NetworkFact
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkInfoNodeMemberMissingAnalyzer extends NetworkInfoAnalyzer {
  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkInfoNodeMemberMissingAnalyzer(context).analyze()
  }
}

class NetworkInfoNodeMemberMissingAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze(): NetworkInfoAnalysisContext = {
    val missingNodeDetails = context.nodeDetails.filter { nodeDetail =>
      if (nodeDetail.definedInRelation) {
        false
      }
      else if (nodeDetail.connection) {
        false
      }
      else {
        (context.proposed && nodeDetail.proposed) ||
          (!context.proposed && !nodeDetail.proposed)
      }
    }

    if (missingNodeDetails.nonEmpty) {
      val fact = NetworkFact(
        Fact.NodeMemberMissing.name,
        Some("node"),
        Some(missingNodeDetails.map(_.id)),
        Some(missingNodeDetails.map(_.toRef)),
        None
      )
      context.copy(
        networkFacts = context.networkFacts :+ fact
      )
    }
    else {
      context
    }
  }
}
