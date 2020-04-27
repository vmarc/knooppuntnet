package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.NetworkType
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.node.NodeAnalysis
import kpn.server.analyzer.engine.analysis.node.analyzers.MainNodeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.stereotype.Component

@Component
class NetworkNodeAnalyzerImpl(
  mainNodeAnalyzer: MainNodeAnalyzer,
  analysisContext: AnalysisContext
) extends NetworkNodeAnalyzer {

  override def analyze(networkType: NetworkType, data: Data): Map[Long, NodeAnalysis] = {
    val nodes = data.nodes.values.toSeq.filter(node => analysisContext.isReferencedNetworkNode(networkType, node.raw))
    val nodeAnalyses = nodes.map(mainNodeAnalyzer.analyze)
    nodeAnalyses.map(n => (n.node.id, n)).toMap
  }

}

