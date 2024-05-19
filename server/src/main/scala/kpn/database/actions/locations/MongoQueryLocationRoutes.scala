package kpn.database.actions.locations

import kpn.api.common.SurveyDateInfo
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.location.BooleanParameter
import kpn.api.common.location.LastUpdatedParameter
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.SurveyParameter
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.core.util.RouteSymbol
import kpn.database.actions.locations.MongoQueryLocationRoutes.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.gte
import org.mongodb.scala.model.Filters.lt
import org.mongodb.scala.model.Filters.not
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

case class LocationRouteInfoData(
  id: Long,
  name: String,
  meters: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Tags,
  broken: Boolean,
  inaccessible: Boolean
)

object MongoQueryLocationRoutes {
  private val log = Log(classOf[MongoQueryLocationRoutes])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val query = new MongoQueryLocationRoutes(database, SurveyDateInfoBuilder.dateInfo)
      query.exploreSurvey(LocationKey(NetworkType.hiking, Country.fr, "fr"))
    }
  }
}

class MongoQueryLocationRoutes(database: Database, surveyDateInfo: SurveyDateInfo) {

  def optionsGroups(): Seq[ServerFilterGroup] = {
    //  Seq(
    //    facet(
    //      Facet("years", pipelineYears: _*),
    //      Facet("months", pipelineMonths: _*),
    //    )
    //  )
    Seq.empty
  }

  def exploreSurvey(locationKey: LocationKey): Seq[Bson] = {
    val surveyValue =
      s"""
         |{
         |  survey: {
         |    $$switch: {
         |      branches: [
         |        {
         |          case: {$$not: ["$$lastSurvey"]},
         |          then: "0-unknown"
         |        },
         |        {
         |          case: {$$gte: ["$$lastSurvey", "${surveyDateInfo.lastMonthStart.yyyymmdd}"]},
         |          then: "1-last-month"
         |        },
         |        {
         |          case: {$$gte: ["$$lastSurvey", "${surveyDateInfo.lastHalfYearStart.yyyymmdd}"]},
         |          then: "2-last-half-year"
         |        },
         |        {
         |          case: {$$gte: ["$$lastSurvey", "${surveyDateInfo.lastYearStart.yyyymmdd}"]},
         |          then: "3-last-year"
         |        },
         |        {
         |          case: {$$gte: ["$$lastSurvey", "${surveyDateInfo.lastTwoYearsStart.yyyymmdd}"]},
         |          then: "4-last-two-years"
         |        },
         |      ],
         |      default: "5-older"
         |    }
         |  }
         |}
         |""".stripMargin

    Seq(
      filter(buildFilter(locationKey, LocationRoutesParameters() /* !!! */)),
      project(
        BsonDocument(surveyValue),
      ),
      group(
        "$survey",
        sum("count", 1)
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          excludeId(),
          computed("_id", BsonDocument("""{$substr: ["$_id", 2, 99]}""")),
          include("count")
        )
      )
    ) ++ optionGroupPipeline("survey")
  }

  def optionGroupProposedPipeline(locationKey: LocationKey, parameters: LocationRoutesParameters): Seq[Bson] = {
    Seq(
      filter(buildFilter(locationKey, parameters)),
      project(
        fields(
          excludeId(),
          computed("proposed", BsonDocument("""{ $cond: [ "$proposed", "yes", "no" ]}"""))
        )
      ),
      group(
        "$proposed",
        sum("count", 1)
      ),
    ) ++ optionGroupPipeline("proposed")
  }

  def optionGroupFactsPipeline(locationKey: LocationKey, parameters: LocationRoutesParameters): Seq[Bson] = {
    Seq(
      filter(buildFilter(locationKey, parameters)),
      unwind("$labels"),
      filter(
        BsonDocument("""{labels: {$regex: "fact-.*"}}""")
      ),
      project(
        fields(
          BsonDocument("""{name: {$substr: ["$labels", 5, 99]}}""")
        )
      ),
      group(
        "$name",
        sum("count", 1)
      ),
    ) ++ optionGroupPipeline("facts")
  }

  private def optionGroupPipeline(groupName: String): Seq[Bson] = {
    Seq(
      project(
        fields(
          excludeId(),
          computed(
            "options",
            fields(
              computed("name", "$_id"),
              computed("count", "$count"),
            )
          )
        )
      ),
      sort(orderBy(ascending("name"))),
      group(
        groupName,
        push("options", "$options")
      ),
      project(
        fields(
          excludeId(),
          computed("name", "$_id"),
          include("options")
        )
      )
    )
  }

  def countDocuments(locationKey: LocationKey, parameters: LocationRoutesParameters): Long = {
    val filter = buildFilter(locationKey, parameters)
    database.routes.countDocuments(filter, log)
  }

  def find(
    locationKey: LocationKey,
    parameters: LocationRoutesParameters
  ): Seq[LocationRouteInfo] = {

    val pipeline = Seq(
      filter(buildFilter(locationKey, parameters)),
      sort(orderBy(ascending("summary.name", "summary.id"))),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt),
      project(
        fields(
          excludeId(),
          computed("id", "$summary.id"),
          computed("name", "$summary.name"),
          computed("meters", "$summary.meters"),
          include("lastUpdated"),
          include("lastSurvey"),
          computed("tags", "$summary.tags"),
          computed("broken", "$summary.broken"),
          computed("inaccessible", "$summary.inaccessible")
        )
      )
    )

    log.debugElapsed {
      val docs = database.routes.aggregate[LocationRouteInfoData](pipeline).zipWithIndex.map { case (doc, index) =>
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        val symbol = RouteSymbol.from(doc.tags)
        LocationRouteInfo(
          rowIndex = rowIndex,
          id = doc.id,
          name = doc.name,
          meters = doc.meters,
          lastUpdated = doc.lastUpdated,
          lastSurvey = doc.lastSurvey,
          symbol = symbol,
          broken = doc.broken,
          inaccessible = doc.inaccessible
        )
      }
      (s"location routes: ${docs.size}", docs)
    }
  }

  private def buildFilter(locationKey: LocationKey, parameters: LocationRoutesParameters): Bson = {
    val filters: Seq[Bson] = Seq(
      Some(equal("labels", Label.active)),
      Some(equal("labels", Label.networkType(locationKey.networkType))),
      Some(equal("labels", Label.location(locationKey.name))),
      parameters.fact.map { fact =>
        equal("labels", Label.fact(fact))
      },
      parameters.survey.map {
        case SurveyParameter.unknown => not(equal("labels", "survey"))
        case SurveyParameter.lastMonth =>
          and(
            equal("labels", "survey"),
            gte("lastSurvey", surveyDateInfo.lastMonthStart.yyyymmdd)
          )
        case SurveyParameter.lastHalfYear =>
          and(
            equal("labels", "survey"),
            lt("lastSurvey", surveyDateInfo.lastMonthStart.yyyymmdd),
            gte("lastSurvey", surveyDateInfo.lastHalfYearStart.yyyymmdd)
          )
        case SurveyParameter.lastYear =>
          and(
            equal("labels", "survey"),
            lt("lastSurvey", surveyDateInfo.lastHalfYearStart.yyyymmdd),
            gte("lastSurvey", surveyDateInfo.lastYearStart.yyyymmdd)
          )
        case SurveyParameter.lastTwoYears =>
          and(
            equal("labels", "survey"),
            lt("lastSurvey", surveyDateInfo.lastYearStart.yyyymmdd),
            gte("lastSurvey", surveyDateInfo.lastTwoYearsStart.yyyymmdd)
          )
        case SurveyParameter.older =>
          and(
            equal("labels", "survey"),
            lt("lastSurvey", surveyDateInfo.lastTwoYearsStart.yyyymmdd)
          )
      },
      parameters.lastUpdated.map {
        case LastUpdatedParameter.lastWeek =>
          gte("lastUpdated", surveyDateInfo.lastWeekStart.yyyymmdd)
        case LastUpdatedParameter.lastYear =>
          and(
            lt("lastUpdated", surveyDateInfo.lastWeekStart.yyyymmdd),
            gte("lastUpdated", surveyDateInfo.lastYearStart.yyyymmdd)
          )
        case LastUpdatedParameter.older =>
          lt("lastUpdated", surveyDateInfo.lastYearStart.yyyymmdd)
      },
      parameters.proposed.map {
        case BooleanParameter.yes => equal("proposed", true)
        case BooleanParameter.no => equal("proposed", false)
      },
    ).flatten
    and(filters: _*)
  }
}
