package kpn.server.analyzer.engine.analysis.network.base

import kpn.api.common.data.raw.RawRelation
import kpn.core.doc.BaseNetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkAnalysisContext
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkNameAnalyzer
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkTypeAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class BaseNetworkMainAnalyzer {

  def analyze(relation: RawRelation): Option[BaseNetworkDoc] = {
    Log.context(f"network=${relation.id}%07d") {
      val context = BaseNetworkAnalysisContext(relation)
      val analyzers: List[BaseNetworkAnalyzer] = List(
        BaseNetworkTypeAnalyzer,
        BaseNetworkNameAnalyzer,
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[BaseNetworkAnalyzer], context: BaseNetworkAnalysisContext): Option[BaseNetworkDoc] = {
    if (context.abort) {
      None
    }
    else if (analyzers.isEmpty) {
      Some(
        BaseNetworkDoc(
          _id = context.relation.id,
          active = true,
          routeType = context.routeType,
          routeScope = context.routeScope,
          name = context.name,
          version = context.relation.version,
          timestamp = context.relation.timestamp,
          changeSetId = context.relation.changeSetId,
          members = context.relation.members,
          tags = context.relation.tags,
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
