package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NodeNetworkReferencesAnalyzer(networkRepository: NetworkRepository) extends NodeAnalyzer {
  override def analyze(context: NodeAnalysisContext): NodeAnalysisContext = {
    val networkReferences = networkRepository.nodeBaseNetworkReferences(context.node._id)
    context.copy(_networkReferences = Some(networkReferences))
  }
}
