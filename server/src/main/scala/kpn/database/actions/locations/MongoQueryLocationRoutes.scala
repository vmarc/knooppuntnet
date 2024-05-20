package kpn.database.actions.locations

import kpn.api.common.SurveyDateInfo
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.changes.filter.ServerFilterOption
import kpn.api.common.location.BooleanParameter
import kpn.api.common.location.LastUpdatedParameter
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRouteOptions
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.SurveyParameter
import kpn.api.custom.Day
import kpn.api.custom.LocationKey
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.core.util.RouteSymbol
import kpn.database.base.Database
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Facet
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

case class Groups(
  facts: Seq[ServerFilterGroup],
  proposed: Seq[ServerFilterGroup],
  survey: Seq[ServerFilterGroup],
  lastUpdated: Seq[ServerFilterGroup],
)

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

class MongoQueryLocationRoutes(database: Database, surveyDateInfo: SurveyDateInfo) {

  private val log = Log(classOf[MongoQueryLocationRoutes])

  def filterOptions(locationKey: LocationKey, parameters: LocationRoutesParameters): LocationRouteOptions = {
    val pipeline = Seq(
      filter(and(mainFilters(locationKey): _*)),
      facet(
        Facet("facts", factsPipeline(parameters): _*),
        Facet("proposed", proposedPipeline(parameters): _*),
        Facet("survey", surveyPipeline(parameters): _*),
        Facet("lastUpdated", lastUpdatedPipeline(parameters): _*),
      )
    )

    val groups = database.routes.aggregate[Groups](pipeline)

    val proposed = {
      val options = groups.flatMap(_.proposed).flatMap(_.options)
      val oo = if (options.size != 1) {
        Seq(ServerFilterOption("all", options.map(_.count).sum)) ++ options
      }
      else {
        options
      }

      val selected = if (oo.size == 1) {
        oo.head.name
      }
      else {
        parameters.proposed match {
          case None => "all"
          case Some(proposed) =>
            oo.find(_.name == proposed.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, oo)
    }

    val totalCount = proposed.options.map(_.count).sum

    val fact = {
      val factOptions = groups.flatMap(_.facts).flatMap(_.options).sortBy(_.name)
      val options = Seq(ServerFilterOption("all", totalCount)) ++ factOptions
      val selected = parameters.fact match {
        case None => "all"
        case Some(f) => f.name
      }
      ServerFilterGroup(selected, options)
    }

    val survey = {
      val surveyOptions = groups.flatMap(_.survey).flatMap(_.options)
      val options = if (surveyOptions.size != 1) {
        Seq(ServerFilterOption("all", surveyOptions.map(_.count).sum)) ++ surveyOptions
      }
      else {
        surveyOptions
      }

      val selected = if (options.size == 1) {
        options.head.name
      }
      else {
        parameters.survey match {
          case None => "all"
          case Some(value) =>
            surveyOptions.find(_.name == value.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    val lastUpdated = {
      val lastUpdatedOptions = groups.flatMap(_.lastUpdated).flatMap(_.options).sortBy(_.name)
      val options = if (lastUpdatedOptions.size != 1) {
        Seq(ServerFilterOption("all", lastUpdatedOptions.map(_.count).sum)) ++ lastUpdatedOptions
      }
      else {
        lastUpdatedOptions
      }

      val selected = if (options.size == 1) {
        options.head.name
      }
      else {
        parameters.lastUpdated match {
          case None => "all"
          case Some(value) =>
            options.find(_.name == value.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    LocationRouteOptions(
      fact,
      survey,
      lastUpdated,
      proposed
    )
  }

  private def surveyPipeline(parameters: LocationRoutesParameters): Seq[Bson] = {
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
         |          then: "1-lastMonth"
         |        },
         |        {
         |          case: {$$gte: ["$$lastSurvey", "${surveyDateInfo.lastHalfYearStart.yyyymmdd}"]},
         |          then: "2-lastHalfYear"
         |        },
         |        {
         |          case: {$$gte: ["$$lastSurvey", "${surveyDateInfo.lastYearStart.yyyymmdd}"]},
         |          then: "3-lastYear"
         |        },
         |        {
         |          case: {$$gte: ["$$lastSurvey", "${surveyDateInfo.lastTwoYearsStart.yyyymmdd}"]},
         |          then: "4-lastTwoYears"
         |        },
         |      ],
         |      default: "5-older"
         |    }
         |  }
         |}
         |""".stripMargin

    val parameterFilters = Seq(
      factFilter(parameters),
      lastUpdatedFilter(parameters),
      proposedFilter(parameters),
    ).flatten

    val xx = if (parameterFilters.isEmpty) {
      Seq.empty
    }
    else if (parameterFilters.size == 1) {
      Seq(
        filter(parameterFilters.head)
      )
    }
    else {
      Seq(
        filter(and(parameterFilters: _*))
      )
    }

    xx ++ Seq(
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

  private def lastUpdatedPipeline(parameters: LocationRoutesParameters): Seq[Bson] = {
    val lastUpdatedValue =
      s"""
         |{
         |  lastUpdatedValue: {
         |    $$switch: {
         |      branches: [
         |        {
         |          case: {$$gte: ["$$lastUpdated", "${surveyDateInfo.lastWeekStart.yyyymmdd}"]},
         |          then: "1-lastWeek"
         |        },
         |        {
         |          case: {$$gte: ["$$lastUpdated", "${surveyDateInfo.lastYearStart.yyyymmdd}"]},
         |          then: "2-lastYear"
         |        },
         |      ],
         |      default: "3-older"
         |    }
         |  }
         |}
         |""".stripMargin

    val parameterFilters = Seq(
      factFilter(parameters),
      surveyFilter(parameters),
      proposedFilter(parameters),
    ).flatten

    val xx = if (parameterFilters.isEmpty) {
      Seq.empty
    }
    else if (parameterFilters.size == 1) {
      Seq(
        filter(parameterFilters.head)
      )
    }
    else {
      Seq(
        filter(and(parameterFilters: _*))
      )
    }

    xx ++ Seq(
      project(
        BsonDocument(lastUpdatedValue),
      ),
      group(
        "$lastUpdatedValue",
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
    ) ++ optionGroupPipeline("lastUpdatedValue")
  }

  private def proposedPipeline(parameters: LocationRoutesParameters): Seq[Bson] = {
    val parameterFilters = Seq(
      factFilter(parameters),
      surveyFilter(parameters),
      lastUpdatedFilter(parameters),
    ).flatten

    val xx = if (parameterFilters.isEmpty) {
      Seq.empty
    }
    else if (parameterFilters.size == 1) {
      Seq(
        filter(parameterFilters.head)
      )
    }
    else {
      Seq(
        filter(and(parameterFilters: _*))
      )
    }

    xx ++ Seq(
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

  private def factsPipeline(parameters: LocationRoutesParameters): Seq[Bson] = {

    val parameterFilters = Seq(
      surveyFilter(parameters),
      lastUpdatedFilter(parameters),
      proposedFilter(parameters),
    ).flatten

    val xx = if (parameterFilters.isEmpty) {
      Seq.empty
    }
    else if (parameterFilters.size == 1) {
      Seq(
        filter(parameterFilters.head)
      )
    }
    else {
      Seq(
        filter(and(parameterFilters: _*))
      )
    }

    xx ++ Seq(
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
      skip(parameters.pageSize.toInt * parameters.pageIndex.toInt),
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

  private def mainFilters(locationKey: LocationKey): Seq[Bson] = {
    Seq(
      equal("labels", Label.active),
      equal("labels", Label.networkType(locationKey.networkType)),
      equal("labels", Label.location(locationKey.name)),
    )
  }

  private def buildFilter(locationKey: LocationKey, parameters: LocationRoutesParameters): Bson = {
    val filters: Seq[Bson] = mainFilters(locationKey) ++ Seq(
      factFilter(parameters),
      surveyFilter(parameters),
      lastUpdatedFilter(parameters),
      proposedFilter(parameters)
    ).flatten
    and(filters: _*)
  }

  private def factFilter(parameters: LocationRoutesParameters): Option[Bson] = {
    parameters.fact.map { fact =>
      equal("labels", Label.fact(fact))
    }
  }

  private def surveyFilter(parameters: LocationRoutesParameters): Option[Bson] = {
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
    }
  }

  private def lastUpdatedFilter(parameters: LocationRoutesParameters): Option[Bson] = {

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
    }
  }

  private def proposedFilter(parameters: LocationRoutesParameters): Option[Bson] = {
    parameters.proposed.map {
      case BooleanParameter.yes => equal("proposed", true)
      case BooleanParameter.no => equal("proposed", false)
    }
  }
}
