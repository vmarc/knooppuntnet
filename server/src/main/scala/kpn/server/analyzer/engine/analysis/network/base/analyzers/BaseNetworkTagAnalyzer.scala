package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.core.analysis.TagInterpreter

object BaseNetworkTagAnalyzer extends BaseNetworkAnalyzer {

  override def analyze(context: BaseNetworkAnalysisContext): BaseNetworkAnalysisContext = {
    if (TagInterpreter.isNetworkRelation(context.relation)) {
      context
    }
    else {
      context.copy(abort = true)
    }
  }
}
