package kpn.server.analyzer.engine.analysis.node.main

import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeAnalysisContext
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeAnalyzer

import scala.annotation.tailrec

class NodeMainAnalyzer {

  def analyze(node: BaseNodeDoc): Option[NodeDoc] = {
    Log.context(f"node=${node._id}%07d") {
      val context = NodeAnalysisContext(node)
      val analyzers: List[NodeAnalyzer] = List(
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
          labels = Seq.empty,
          country = context.node.country,
          name = context.node.name,
          names = context.node.names,
          version = context.node.version,
          changeSetId = context.node.changeSetId,
          latitude = context.node.latitude,
          longitude = context.node.longitude,
          lastUpdated = context.node.lastUpdated,
          lastSurvey = None,
          tags = context.node.tags,
          facts = Seq.empty,
          locations = context.node.locations,
          integrity = None,
          routeReferences = Seq.empty
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
