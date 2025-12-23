package kpn.server.analyzer.engine.analysis.network.base

import kpn.api.common.data.raw.Raw
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.network.NetworkBaseData
import kpn.core.doc.BaseNetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkAnalysisContext
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkNameAnalyzer
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkTagAnalyzer
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkTypeAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class BaseNetworkMainAnalyzer {

  def analyze(relation: RawRelation): Option[BaseNetworkDoc] = {
    Log.context(f"network=${relation.id}%07d") {
      val context = BaseNetworkAnalysisContext(relation)
      val analyzers: List[BaseNetworkAnalyzer] = List(
        BaseNetworkTagAnalyzer,
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
      val nodeIds = context.relation.nodeMembers.map(_.ref).sorted
      val relationIds = context.relation.relationMembers.map(_.ref).sorted
      Some(
        BaseNetworkDoc(
          _id = context.relation.id,
          active = true,
          NetworkBaseData(
            raw = Raw(
              version = context.relation.version,
              changeSetId = context.relation.changeSetId,
              timestamp = context.relation.timestamp,
              tags = context.relation.tags,
            ),
            name = context.name,
            routeType = context.routeType,
            routeScope = context.routeScope,
            members = context.relation.members,
          ),
          nodeIds = nodeIds,
          relationIds = relationIds,
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
