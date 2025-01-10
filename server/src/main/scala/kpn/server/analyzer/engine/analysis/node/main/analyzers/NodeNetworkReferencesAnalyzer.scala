package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeNetworkReferencesAnalyzer(nodeRepository: NodeRepository) extends NodeAnalyzer {
  override def analyze(context: NodeAnalysisContext): NodeAnalysisContext = {
    val networkReferences = nodeRepository.nodeNetworkReferences(context.node._id)
    context.copy(_networkReferences = Some(networkReferences))
  }
}
