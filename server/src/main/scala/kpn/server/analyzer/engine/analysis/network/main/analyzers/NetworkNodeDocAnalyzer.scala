package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.data.MemberType
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NetworkNodeDocAnalyzer(nodeRepository: NodeRepository) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkNodeDocAnalyzer])

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    val nodeDocs = loadNodeDocs(context)
    context.copy(
      _nodeDocs = Some(nodeDocs)
    )
  }

  private def loadNodeDocs(context: NetworkAnalysisContext): Seq[NodeDoc] = {
    if (context.network.active) {
      val nodeIds = allNodeIds(context)
      nodeRepository.activeNodesWithIds(nodeIds)
    }
    else {
      Seq.empty
    }
  }

  private def allNodeIds(context: NetworkAnalysisContext): Seq[Long] = {
    val routeNodeIds = context.routeDetails.flatMap(_.networkNodeIds.toSeq.flatten)
    val networkNodeIds = context.network.members.filter(_.memberType == MemberType.Node).map(_.ref)
    (networkNodeIds ++ routeNodeIds).distinct.sorted
  }
}
