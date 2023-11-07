package kpn.api.common.monitor

import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.util.RouteSymbol
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer

import scala.util.Failure
import scala.util.Success

object MonitorRouteRelation {

  def from(relation: Relation, role: Option[String]): MonitorRouteRelation = {

    val nameTagKeys = Seq(
      "name",
      "name:de",
      "name:nl",
      "name:fr",
      "name:en",
    )

    val names = nameTagKeys.flatMap(nameTagKey => relation.tags(nameTagKey))

    val name = names.headOption match {
      case Some(name) => name
      case None =>
        relation.tags("from") match {
          case None => "?" // TODO get  name from 'name:fr', 'name:nl', etc.
          case Some(from) =>
            relation.tags("to") match {
              case None => "?"
              case Some(to) => s"$from — $to"
            }
        }
    }

    val survey = SurveyDateAnalyzer.analyze(relation.tags) match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }
    val symbol = RouteSymbol.from(relation.tags)

    val relations = relation.relationMembers.filterNot(_.role.contains("place_of_worship")).map { member =>
      MonitorRouteRelation.from(member.relation, member.role)
    }

    val subRelationsDistance = relations.map(_.osmDistance).sum

    MonitorRouteRelation(
      relationId = relation.id,
      name = name,
      role = role,
      survey = survey,
      symbol = symbol,
      referenceTimestamp = None,
      referenceFilename = None,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmWayCount = 0,
      osmSegmentCount = 0,
      osmDistance = 0,
      osmDistanceSubRelations = subRelationsDistance,
      gaps = None,
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
  symbol: Option[String],

  /*
    Reference details are only filled in when the route reference type is "multi-gpx".
    Values are None when this MonitorRouteRelation represents the main super route relation
    and that relation has no ways itself, or any subrelation without ways.
   */
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceDistance: Long,

  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmSegmentCount: Long,
  osmDistance: Long,
  osmDistanceSubRelations: Long,
  gaps: Option[String],
  happy: Boolean,
  relations: Seq[MonitorRouteRelation]
)
