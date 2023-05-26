package kpn.api.common.monitor

import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer

import scala.util.Failure
import scala.util.Success

object MonitorRouteRelation {

  def from(relation: Relation, role: Option[String]): MonitorRouteRelation = {

    val name = relation.tags("name") match {
      case Some(name) => name
      case None =>
        relation.tags("from") match {
          case None => "?" // TODO get  name from 'name:fr', 'name:nl', etc.
          case Some(from) =>
            relation.tags("to") match {
              case None => "?"
              case Some(to) => s"$from-$to"
            }
        }
    }

    val survey = SurveyDateAnalyzer.analyze(relation.tags) match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }
    val osmWayCount = relation.wayMembers.size
    val osmDistance = relation.wayMembers.map(w => Haversine.meters(w.way.nodes)).sum

    val relations = relation.relationMembers.filterNot(_.role.contains("place_of_worship")).map { member =>
      MonitorRouteRelation.from(member.relation, member.role)
    }

    val subRelationsDistance = relations.map(_.osmDistance).sum

    MonitorRouteRelation(
      relationId = relation.id,
      name = name,
      role = role,
      survey = survey,
      referenceDay = None,
      referenceFilename = None,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmWayCount = osmWayCount,
      osmDistance = osmDistance + subRelationsDistance,
      osmSegmentCount = 0,
      happy = false,
      relations = relations
    )
  }
}

case class MonitorRouteRelation(
  relationId: Long,
  name: String,
  role: Option[String],
  survey: Option[Day],

  /*
    Reference details are only filled in when the route reference type is "multi-gpx".
    Values are None when this MonitorRouteRelation represents the main super route relation
    and that relation has no ways itself, or any subrelation without ways.
   */
  referenceDay: Option[Day],
  referenceFilename: Option[String],
  referenceDistance: Long,

  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  happy: Boolean,
  relations: Seq[MonitorRouteRelation]
)
