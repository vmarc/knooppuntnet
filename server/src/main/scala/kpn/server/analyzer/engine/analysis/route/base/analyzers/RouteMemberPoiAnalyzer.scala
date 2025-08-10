package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.data.Member
import kpn.core.poi.PoiConfiguration

object RouteMemberPoiAnalyzer {

  def analyze(member: Member): Option[String] = {
    val tags = member.tags

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
