package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.WayMember
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTagAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo

object RouteNodeTagAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteNodeTagAnalyzer(context).analyze
  }
}

class RouteNodeTagAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val nodes = findReferencedNodes()
    val routeNodeInfos = nodes.flatMap { node =>
      NodeTagAnalyzer.analyze(node.tags).flatMap { analysis =>
        analysis.nodeNames.find(_.scopedNetworkType == context.scopedNetworkType).map { nodeName =>
          node.id -> RouteNodeInfo(node, nodeName.name)
        }
      }
    }.toMap
    context.copy(
      routeNodeInfos = routeNodeInfos
    )
  }

  private def findReferencedNodes(): Seq[Node] = {
    context.relation.members.flatMap { member =>
      member match {
        case nodeMember: NodeMember => Set(nodeMember.node)
        case wayMember: WayMember => wayMember.way.nodes
        case _ => Seq.empty
      }
    }.distinct
  }

}
