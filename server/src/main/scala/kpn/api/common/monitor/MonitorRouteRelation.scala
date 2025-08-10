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
      "ref",
    )

    val names = nameTagKeys.flatMap(nameTagKey => relation.tagValues(nameTagKey))

    val name = names.headOption match {
      case Some(nameValue) => nameValue
      case None =>
        relation.tagValue("from") match {
          case None => "?" // TODO get  name from 'name:fr', 'name:nl', etc.
          case Some(from) =>
            relation.tagValue("to") match {
              case None => "?"
              case Some(to) => s"$from — $to"
            }
        }
    }

    val survey = SurveyDateAnalyzer.analyze(relation) match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }
    val symbol = RouteSymbol.from(relation)

    val relations = relation.relationMembers.filterNot(_.role.contains("place_of_worship")).flatMap { member =>
      member.relation.map(relation => MonitorRouteRelation.from(relation, member.role))
    }

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
      happy = true,
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
  happy: Boolean,
  relations: Seq[MonitorRouteRelation]
)
