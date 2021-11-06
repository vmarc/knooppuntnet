package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.NodeMember
import kpn.api.common.data.WayMember
import kpn.core.util.Unique
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute
import org.springframework.stereotype.Component

@Component
class RouteNodeInfoAnalyzerImpl(analysisContext: AnalysisContext, oldNodeAnalyzer: OldNodeAnalyzer) extends RouteNodeInfoAnalyzer {
  def analyze(loadedRoute: LoadedRoute): Map[Long, RouteNodeInfo] = {
    val nodes = loadedRoute.relation.members.flatMap {
      case nodeMember: NodeMember => Seq(nodeMember.node)
      case wayMember: WayMember => wayMember.way.nodes
      case _ => Seq.empty
    }
    Unique.filter(nodes).filter(node => analysisContext.isValidNetworkNode(node.raw)).flatMap { node =>
      oldNodeAnalyzer.scopedName(loadedRoute.scopedNetworkType, node.tags) match {
        case None => None
        case Some(name) =>
          val longName = oldNodeAnalyzer.scopedLongName(loadedRoute.scopedNetworkType, node.tags) match {
            case None => None
            case Some(scopedLongName) =>
              if (name != scopedLongName) {
                Some(scopedLongName)
              }
              else {
                None
              }
          }
          Some(node.id -> RouteNodeInfo(node, name, longName))
      }
    }.toMap
  }
}
