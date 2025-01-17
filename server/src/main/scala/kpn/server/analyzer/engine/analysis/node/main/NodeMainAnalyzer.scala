package kpn.server.analyzer.engine.analysis.node.main

import kpn.api.base.ObjectId
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeAnalysisContext
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeIntegrityAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeLabelsAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeRouteReferencesAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class NodeMainAnalyzer(
  nodeRouteReferencesAnalyzer: NodeRouteReferencesAnalyzer,
  nodeNetworkReferencesAnalyzer: NodeNetworkReferencesAnalyzer,
) {

  def analyze(node: BaseNodeDoc): Option[NodeDoc] = {
    Log.context(f"node=${node._id}%07d") {
      val context = NodeAnalysisContext(node, facts = node.facts)
      val analyzers: List[NodeAnalyzer] = List(
        nodeRouteReferencesAnalyzer,
        nodeNetworkReferencesAnalyzer,
        NodeIntegrityAnalyzer,
        NodeLabelsAnalyzer
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[NodeAnalyzer], context: NodeAnalysisContext): Option[NodeDoc] = {
    if (analyzers.isEmpty) {
      Some(
        NodeDoc(
          _id = context.node._id,
          labels = context.labels,
          country = context.node.country,
          name = context.node.name,
          names = context.node.names,
          version = context.node.version,
          changeSetId = context.node.changeSetId,
          latitude = context.node.latitude,
          longitude = context.node.longitude,
          lastUpdated = context.node.lastUpdated,
          lastSurvey = context.node.lastSurvey,
          tags = context.node.tags,
          facts = context.facts,
          locations = context.node.locations,
          integrity = context.integrity,
          routeReferences = context.routeReferences,
          networkReferences = context.networkReferences,
          Some(ObjectId())
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
