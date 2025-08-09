package kpn.database.actions.locations

import com.mongodb.client.model.Accumulators.push
import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.count
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.elemMatch
import com.mongodb.client.model.Filters.gte
import com.mongodb.client.model.Filters.lt
import com.mongodb.client.model.Filters.not
import com.mongodb.client.model.Filters.or
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.Fact
import kpn.api.common.SurveyDateInfo
import kpn.api.common.location.BooleanParameter
import kpn.api.common.location.LastUpdatedParameter
import kpn.api.common.location.SurveyParameter
import kpn.core.doc.Label
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson

object LocationQuery {

  def locationFilter(fieldName: String, subset: LocationSubset): Bson = {
    if (subset.locationIds.sizeIs == 1) {
      equal(fieldName, Label.location(subset.locationIds.head))
    }
    else {
      val locationComparisons = subset.locationIds.map { locationId =>
        equal(fieldName, Label.location(locationId))
      }
      or(locationComparisons: _*)
    }
  }

  def changesLocationFilter(fieldName: String, subset: LocationSubset): Bson = {
    if (subset.locationIds.sizeIs == 1) {
      equal(fieldName, subset.locationIds.head)
    }
    else {
      val locationComparisons = subset.locationIds.map { locationId =>
        equal(fieldName, locationId)
      }
      or(locationComparisons: _*)
    }
  }

  def surveyFilter(surveyDateInfo: SurveyDateInfo, survey: Option[SurveyParameter]): Option[Bson] = {
    survey.map {
      case SurveyParameter.Unknown => not(equal("labels", "survey"))
      case SurveyParameter.LastMonth =>
        and(
          equal("labels", "survey"),
          gte("lastSurvey", surveyDateInfo.lastMonthStart.yyyymmdd)
        )
      case SurveyParameter.LastHalfYear =>
        and(
          equal("labels", "survey"),
          lt("lastSurvey", surveyDateInfo.lastMonthStart.yyyymmdd),
          gte("lastSurvey", surveyDateInfo.lastHalfYearStart.yyyymmdd)
        )
      case SurveyParameter.LastYear =>
        and(
          equal("labels", "survey"),
          lt("lastSurvey", surveyDateInfo.lastHalfYearStart.yyyymmdd),
          gte("lastSurvey", surveyDateInfo.lastYearStart.yyyymmdd)
        )
      case SurveyParameter.LastTwoYears =>
        and(
          equal("labels", "survey"),
          lt("lastSurvey", surveyDateInfo.lastYearStart.yyyymmdd),
          gte("lastSurvey", surveyDateInfo.lastTwoYearsStart.yyyymmdd)
        )
      case SurveyParameter.Older =>
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
      case BooleanParameter.Yes => equal("proposed", true)
      case BooleanParameter.No => equal("proposed", false)
    }
  }

  def factFilter(fact: Option[Fact]): Option[Bson] = {
    fact.map { fact =>
      equal("labels", Label.fact(fact))
    }
  }

  def integrityCheckFilter(subset: LocationSubset, integrityCheck: Option[BooleanParameter]): Option[Bson] = {
    val condition = equal("labels", s"integrity-check-${subset.routeType.entryName}")
    booleanFilter(integrityCheck, condition)
  }

  def integrityCheckFailedFilter(subset: LocationSubset, integrityCheckFailed: Option[BooleanParameter]): Option[Bson] = {
    val condition = equal("labels", s"integrity-check-failed-${subset.routeType.entryName}")
    booleanFilter(integrityCheckFailed, condition)
  }

  def referencedInRoutesFilter(subset: LocationSubset, referencedInRoutes: Option[BooleanParameter]): Option[Bson] = {
    booleanFilter(referencedInRoutes, referencedInRoutesCondition(subset))
  }

  private def referencedInRoutesCondition(subset: LocationSubset): Bson = {
    elemMatch("routeReferences", equal("routeType", subset.routeType.entryName))
  }

  private def booleanFilter(booleanParameter: Option[BooleanParameter], condition: Bson): Option[Bson] = {
    booleanParameter.map {
      case BooleanParameter.Yes => condition
      case BooleanParameter.No => not(condition)
    }
  }

  def proposedPipeline(otherFilters: Seq[Option[Bson]]): MongoPipeline = {
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

  def lastUpdatedPipeline(surveyDateInfo: SurveyDateInfo, otherFilters: Seq[Option[Bson]]): MongoPipeline = {
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

  def surveyPipeline(surveyDateInfo: SurveyDateInfo, otherFilters: Seq[Option[Bson]]): MongoPipeline = {
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

  def factsPipeline(otherFilters: Seq[Option[Bson]]): MongoPipeline = {
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

  def countPipeline(otherFilters: Seq[Option[Bson]]): MongoPipeline = {
    prefilter(otherFilters) ++ Seq(
      count()
    )
  }

  def integrityCheckPipeline(subset: LocationSubset, otherFilters: Seq[Option[Bson]]): MongoPipeline = {
    prefilter(otherFilters) ++ Seq(
      filter(
        equal("labels", s"integrity-check-${subset.routeType.entryName}")
      ),
      count()
    )
  }

  def integrityCheckFailedPipeline(subset: LocationSubset, otherFilters: Seq[Option[Bson]]): MongoPipeline = {
    prefilter(otherFilters) ++ Seq(
      filter(
        equal("labels", s"integrity-check-failed-${subset.routeType.entryName}")
      ),
      count()
    )
  }

  def referencedInRoutesPipeline(subset: LocationSubset, otherFilters: Seq[Option[Bson]]): MongoPipeline = {
    prefilter(otherFilters) ++ Seq(
      filter(referencedInRoutesCondition(subset)),
      count()
    )
  }

  private def optionGroupPipeline(groupName: String): MongoPipeline = {
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

  private def prefilter(otherFilters: Seq[Option[Bson]]): MongoPipeline = {
    val filters = otherFilters.flatten
    if (filters.isEmpty) {
      Seq.empty
    }
    else if (otherFilters.sizeIs == 1) {
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
