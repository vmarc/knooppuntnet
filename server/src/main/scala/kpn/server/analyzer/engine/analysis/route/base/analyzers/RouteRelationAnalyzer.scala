package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.custom.Relation

object RouteRelationAnalyzer {

  def orderedNodeIds(relation: Relation): Seq[Long] = {
    val wayNodeIds = relation.members.flatMap(_.wayNodes).map(_.id).toSet
    relation.members.flatMap {
      case m if m.isNode =>
        if (wayNodeIds.contains(m.memberId)) {
          Seq.empty // we prefer the position of the node in the ways over the position in the route relation
        }
        else {
          Seq(m.node.get.id)
        }

      case m if m.isWay => m.wayNodes.map(_.id)
      case _ => Seq.empty
    }
  }
}
