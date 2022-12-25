package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl

import scala.util.Failure
import scala.util.Success

class MonitorRouteRelationBuilder {

  def build(relation: Relation, role: Option[String]): MonitorRouteRelationData = {

    val name = relation.tags("name")
    val from = relation.tags("from")
    val to = relation.tags("to")
    val survey = SurveyDateAnalyzer.analyze(relation.tags) match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }
    val osmWayCount = relation.wayMembers.size

    val relationDatas = MonitorRouteFilter.filterRelationMembers(relation.relationMembers).map { member =>
      build(member.relation, member.role)
    }

    // TODO remove:
    // val subRelationsDistance = relationDatas.map(_.relation.osmDistance).sum

    val wayMembers = MonitorRouteFilter.filterWayMembers(relation.wayMembers)
    val osmSegmentAnalysis = new MonitorRouteOsmSegmentAnalyzerImpl().analyze(wayMembers)

    //    val deviationAnalysis = new MonitorRouteDeviationAnalyzer().analyze(wayMembers.map(_.way), routeReference)
    //    val monitorRouteState = new MonitorRouteStateAnalyzer().analyze(route, reference, routeRelation, now)

    val monitorRouteRelation = MonitorRouteRelation(
      relationId = relation.id,
      name = name,
      from = from,
      to = to,
      role = role,
      survey = survey,
      deviationDistance = 0,
      deviationCount = 0,
      osmWayCount = osmWayCount,
      osmDistance = osmSegmentAnalysis.osmDistance /* + subRelationsDistance*/ ,
      osmSegmentCount = 0,
      happy = false,
      relations = relationDatas.map(_.relation)
    )

    MonitorRouteRelationData(monitorRouteRelation, osmSegmentAnalysis.routeSegments)
  }
}
