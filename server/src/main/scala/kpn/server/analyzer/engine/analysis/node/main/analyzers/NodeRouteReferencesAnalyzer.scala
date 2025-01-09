package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeRouteReferencesAnalyzer(nodeRepository: NodeRepository) extends NodeAnalyzer {
  override def analyze(context: NodeAnalysisContext): NodeAnalysisContext = {
    val routReferences = nodeRepository.nodeRouteReferences(context.node._id)
    context.copy(routeReferences = routReferences)
  }
}
