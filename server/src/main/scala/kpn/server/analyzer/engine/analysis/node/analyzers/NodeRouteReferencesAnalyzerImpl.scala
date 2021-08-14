package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeRouteReferencesAnalyzerImpl(nodeRepository: NodeRepository) extends NodeRouteReferencesAnalyzer {
  override def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    val routReferences = nodeRepository.nodeRouteReferences(analysis.node.id)
    analysis.copy(routeReferences = routReferences)
  }
}
