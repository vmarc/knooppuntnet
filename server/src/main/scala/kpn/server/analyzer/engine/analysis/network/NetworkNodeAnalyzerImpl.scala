package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.NetworkNode
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.MainNodeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.stereotype.Component

@Component
class NetworkNodeAnalyzerImpl(
  analysisContext: AnalysisContext,
  mainNodeAnalyzer: MainNodeAnalyzer,
  nodeAnalyzer: NodeAnalyzer
) extends NetworkNodeAnalyzer {

  override def analyze(scopedNetworkType: ScopedNetworkType, data: Data): Map[Long, NetworkNode] = {
    val nodes = data.nodes.values.toSeq.filter(node => analysisContext.isReferencedNetworkNode(scopedNetworkType, node.raw))
    val nodeAnalyses = nodes.map(mainNodeAnalyzer.analyze)
    val networkNodes = nodeAnalyses.map { a =>
      val name = nodeAnalyzer.scopedName(scopedNetworkType, a.node.tags).get
      NetworkNode(
        a.node,
        name,
        a.country,
        a.location
      )
    }
    networkNodes.map(n => (n.node.id, n)).toMap
  }

}

