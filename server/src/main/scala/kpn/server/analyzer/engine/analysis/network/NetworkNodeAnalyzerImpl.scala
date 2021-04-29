package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.NetworkNode
import kpn.core.data.Data
import kpn.core.util.Log
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

  private val log: Log = Log(classOf[NetworkNodeAnalyzerImpl])

  override def analyze(scopedNetworkType: ScopedNetworkType, data: Data): Map[Long, NetworkNode] = {
    val nodes = data.nodes.values.toSeq.filter(node => analysisContext.isReferencedNetworkNode(scopedNetworkType, node.raw))
    val nodeAnalyses = nodes.map(mainNodeAnalyzer.analyze)
    val networkNodes = nodeAnalyses.flatMap { a =>
      nodeAnalyzer.scopedName(scopedNetworkType, a.node.tags) match {
        case None =>
          log.warn(s"Could not determine name of node with id ${a.node.id}, tags=${a.node.tags}")
          None
        case Some(name) =>
          val longName = nodeAnalyzer.scopedLongName(scopedNetworkType, a.node.tags)
          Some(
            NetworkNode(
              a.node,
              name,
              longName,
              a.country,
              a.location
            )
          )
      }
    }
    networkNodes.map(n => (n.node.id, n)).toMap
  }
}
