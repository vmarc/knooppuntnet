package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.data.Member
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember
import kpn.core.poi.PoiConfiguration

object RouteMemberPoiAnalyzer {

  def analyze(member: Member): Option[String] = {
    val tags = member match {
      case nodeMember: NodeMember => nodeMember.node.tags
      case wayMember: WayMember => wayMember.way.tags
      case relationMember: RelationMember => relationMember.relation.tags
      case _ => Seq.empty
    }
    if (tags.nonEmpty && RouteRoleAnalyzer.isPoiRole(member.role)) {
      val allDefinitions = PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions)
      val definitions = allDefinitions.filter(_.expression.evaluate(tags))
      definitions.map(_.name).distinct.sorted.headOption
    }
    else {
      None
    }
  }
}
