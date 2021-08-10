package kpn.server.analyzer.engine.analysis.node

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeAspectAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeIntegrityAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLabelsAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeNameAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class NodeAnalyzerImpl(
  nodeCountryAnalyzer: NodeCountryAnalyzer,
  nodeTileAnalyzer: NodeTileAnalyzer,
  nodeLocationsAnalyzer: NodeLocationsAnalyzer,
  nodeRouteReferencesAnalyzer: NodeRouteReferencesAnalyzer
) extends NodeAnalyzer {

  override def analyze(analysis: NodeAnalysis): Option[NodeAnalysis] = {
    Log.context("node=%07d".format(analysis.node.id)) {
      val analyzers = List(
        nodeCountryAnalyzer,
        NodeNameAnalyzer,
        NodeSurveyAnalyzer,
        nodeTileAnalyzer, // has prerequisite: NodeNameAnalyzer
        nodeLocationsAnalyzer,
        nodeRouteReferencesAnalyzer,
        NodeIntegrityAnalyzer,
        NodeLabelsAnalyzer
      )
      doAnalyze(analyzers, analysis)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[NodeAspectAnalyzer], analysis: NodeAnalysis): Option[NodeAnalysis] = {
    if (analysis.abort) {
      None
    }
    else if (analyzers.isEmpty) {
      Some(analysis)
    }
    else {
      val newAnalysis = analyzers.head.analyze(analysis)
      doAnalyze(analyzers.tail, newAnalysis)
    }
  }
}
