package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.server.repository.NetworkRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class NodeNetworkReferencesAnalyzer(networkRepository: NetworkRepository) extends NodeAnalyzer {
  override def analyze(context: NodeAnalysisContext): NodeAnalysisContext = {
    val references = networkRepository.nodeBaseNetworkReferences(context.node._id)
    context.copy(_networkRelationReferences = Some(references))
  }
}
