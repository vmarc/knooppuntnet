package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.NetworkNode
import kpn.core.analysis.TagInterpreter
import kpn.core.data.Data
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.OldMainNodeAnalyzer
import org.springframework.stereotype.Component

@Component
class NetworkNodeAnalyzerImpl(
  oldMainNodeAnalyzer: OldMainNodeAnalyzer,
  oldNodeAnalyzer: OldNodeAnalyzer
) extends NetworkNodeAnalyzer {

  private val log: Log = Log(classOf[NetworkNodeAnalyzerImpl])

  override def analyze(scopedNetworkType: ScopedNetworkType, data: Data): Map[Long, NetworkNode] = {
    val nodes = data.nodes.values.toSeq.filter(node => TagInterpreter.isReferencedNetworkNode(scopedNetworkType, node.raw))
    val nodeAnalyses = nodes.map(oldMainNodeAnalyzer.analyze)
    val networkNodes = nodeAnalyses.flatMap { a =>
      oldNodeAnalyzer.scopedName(scopedNetworkType, a.node.tags) match {
        case None =>
          log.warn(s"Could not determine name of node with id ${a.node.id}, tags=${a.node.tags}")
          None
        case Some(name) =>
          val longName = oldNodeAnalyzer.scopedLongName(scopedNetworkType, a.node.tags)
          Some(
            NetworkNode(
              a.node,
              name,
              longName,
              a.country,
              a.locations
            )
          )
      }
    }
    networkNodes.map(n => (n.node.id, n)).toMap
  }
}
