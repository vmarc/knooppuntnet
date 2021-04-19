package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.NodeMember
import kpn.api.common.data.WayMember
import kpn.core.util.Unique
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo
import kpn.server.analyzer.load.data.LoadedRoute
import org.springframework.stereotype.Component

@Component
class RouteNodeInfoAnalyzerImpl(nodeAnalyzer: NodeAnalyzer) extends RouteNodeInfoAnalyzer {
  def analyze(loadedRoute: LoadedRoute): Map[Long, RouteNodeInfo] = {
    val nodes = loadedRoute.relation.members.flatMap {
      case nodeMember: NodeMember => Seq(nodeMember.node)
      case wayMember: WayMember => wayMember.way.nodes
      case _ => Seq()
    }
    Unique.filter(nodes).filter(_.tags.has("network:type", "node_network")).flatMap { node =>
      nodeAnalyzer.scopedName(loadedRoute.scopedNetworkType, node.tags).map { name =>
        node.id -> RouteNodeInfo(node, name)
      }
    }.toMap
  }

}
