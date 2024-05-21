package kpn.database.actions.locations

import kpn.api.common.SurveyDateInfo
import kpn.api.common.location.BooleanParameter
import kpn.api.common.location.LastUpdatedParameter
import kpn.api.common.location.SurveyParameter
import kpn.api.custom.Fact
import kpn.core.doc.Label
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
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

object FilterPipeline {

  def surveyFilter(surveyDateInfo: SurveyDateInfo, survey: Option[SurveyParameter]): Option[Bson] = {
    survey.map {
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

  def lastUpdatedFilter(surveyDateInfo: SurveyDateInfo, lastUpdated: Option[LastUpdatedParameter]): Option[Bson] = {
    lastUpdated.map {
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

  def proposedFilter(proposed: Option[BooleanParameter]): Option[Bson] = {
    proposed.map {
      case BooleanParameter.yes => equal("proposed", true)
      case BooleanParameter.no => equal("proposed", false)
    }
  }

  def factFilter(fact: Option[Fact]): Option[Bson] = {
    fact.map { fact =>
      equal("labels", Label.fact(fact))
    }
  }

  def proposedPipeline(otherFilters: Seq[Option[Bson]]): Seq[Bson] = {

    prefilter(otherFilters) ++ Seq(
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

  def lastUpdatedPipeline(surveyDateInfo: SurveyDateInfo, otherFilters: Seq[Option[Bson]]): Seq[Bson] = {
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

    prefilter(otherFilters) ++ Seq(
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

  def surveyPipeline(surveyDateInfo: SurveyDateInfo, otherFilters: Seq[Option[Bson]]): Seq[Bson] = {
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

    prefilter(otherFilters) ++ Seq(
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

  def factsPipeline(otherFilters: Seq[Option[Bson]]): Seq[Bson] = {
    prefilter(otherFilters) ++ Seq(
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

  private def prefilter(otherFilters: Seq[Option[Bson]]): Seq[Bson] = {
    val filters = otherFilters.flatten
    if (filters.isEmpty) {
      Seq.empty
    }
    else if (otherFilters.size == 1) {
      Seq(
        filter(filters.head)
      )
    }
    else {
      Seq(
        filter(and(filters: _*))
      )
    }
  }
}
