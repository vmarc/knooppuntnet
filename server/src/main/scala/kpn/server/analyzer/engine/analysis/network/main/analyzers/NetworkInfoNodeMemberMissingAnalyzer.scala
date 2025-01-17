package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkFact

object NetworkInfoNodeMemberMissingAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkInfoNodeMemberMissingAnalyzer(context).analyze()
  }
}

class NetworkInfoNodeMemberMissingAnalyzer(context: NetworkAnalysisContext) {

  def analyze(): NetworkAnalysisContext = {
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
        Fact.NodeMemberMissing,
        Some("node"),
        None,
        Some(missingNodeDetails.map(_.toRef)),
        None
      )
      context.copy(
        _networkFacts = Some(context.networkFacts :+ fact)
      )
    }
    else {
      context
    }
  }
}
