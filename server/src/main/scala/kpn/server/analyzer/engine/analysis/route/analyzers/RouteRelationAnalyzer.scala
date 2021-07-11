package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.NodeMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation

class RouteRelationAnalyzer {

  def orderedNodeIds(relation: Relation): Seq[Long] = {
    val wayNodeIds = relation.wayMembers.flatMap(member => member.way.nodes).map(_.id).toSet
    relation.members.flatMap {
      case nodeMember: NodeMember =>
        if (wayNodeIds.contains(nodeMember.node.id)) {
          Seq.empty // we prefer the position of the node in the ways over the position in the route relation
        }
        else {
          Seq(nodeMember.node.id)
        }

      case wayMember: WayMember => wayMember.way.nodes.map(_.id)
      case _ => Seq.empty
    }
  }

}
