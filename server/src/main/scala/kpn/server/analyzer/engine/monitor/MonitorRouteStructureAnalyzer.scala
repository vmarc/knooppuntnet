package kpn.server.analyzer.engine.monitor

import kpn.api.custom.Relation

class MonitorRouteStructureAnalyzer {
  private val memberAnalyzer = new MonitorRouteMemberAnalyzer

  def analyze(relation: Relation): Unit = {
    memberAnalyzer.analyzeRoute(relation).map { monitorRouteMemberGroup =>
      val elementGroups = MonitorRouteElementAnalyzer.analyze(monitorRouteMemberGroup.members)
      println(s"elementGroups.size=${elementGroups.size}")
    }
  }
}
