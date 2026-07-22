package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkFact

class NetworkNodeMemberMissingAnalyzer(context: NetworkAnalysisContext) {

  def analyze(): Option[NetworkFact] = {
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

    Option.when(missingNodeDetails.nonEmpty) {
      NetworkFact(
        Fact.NodeMemberMissing,
        Some("node"),
        None,
        Some(missingNodeDetails.map(_.toRef)),
        None
      )
    }
  }
}
