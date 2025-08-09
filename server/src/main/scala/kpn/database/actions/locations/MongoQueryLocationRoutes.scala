package kpn.database.actions.locations

import com.mongodb.client.model.Aggregates.facet
import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.SurveyDateInfo
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.changes.filter.ServerFilterOption
import kpn.api.common.data.Tagable
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRouteOptions
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.core.util.RouteSymbol
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.ffacet
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.mongodb.scala.bson.conversions.Bson

case class RouteFilterOptionQueryResult(
  factsTotalRouteCount: Seq[CountResult],
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
  tags: Seq[Tag],
  broken: Boolean,
  inaccessible: Boolean
) extends Tagable

class MongoQueryLocationRoutes(database: Database, surveyDateInfo: SurveyDateInfo) {

  private val log = Log(classOf[MongoQueryLocationRoutes])

  def filterOptions(subset: LocationSubset, parameters: LocationRoutesParameters): LocationRouteOptions = {
    val pipeline = Seq(
      filter(and(mainFilters(subset): _*)),
      facet(
        ffacet("factsTotalRouteCount", factsTotalRouteCountPipeline(parameters)),
        ffacet("facts", factsPipeline(parameters)),
        ffacet("proposed", proposedPipeline(parameters)),
        ffacet("survey", surveyPipeline(parameters)),
        ffacet("lastUpdated", lastUpdatedPipeline(parameters)),
      )
    )

    val groups = database.routes.aggregate(pipeline, classOf[RouteFilterOptionQueryResult])

    val proposed = {
      val options = groups.flatMap(_.proposed).flatMap(_.options)
      val oo = if (options.sizeIs != 1) {
        Seq(ServerFilterOption("all", options.map(_.count).sum)) ++ options.sortBy(_.name)
      }
      else {
        options
      }

      val selected = if (oo.sizeIs == 1) {
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

    val fact = {
      val totalCount = groups.flatMap(_.factsTotalRouteCount).map(_.count).sum
      val factOptions = groups.flatMap(_.facts).flatMap(_.options).sortBy(_.name)
      val options = Seq(ServerFilterOption("all", totalCount)) ++ factOptions
      val selected = parameters.fact match {
        case None => "all"
        case Some(f) => f.entryName
      }
      ServerFilterGroup(selected, options)
    }

    val survey = {
      val surveyOptions = groups.flatMap(_.survey).flatMap(_.options)
      val options = if (surveyOptions.sizeIs != 1) {
        Seq(ServerFilterOption("all", surveyOptions.map(_.count).sum)) ++ surveyOptions
      }
      else {
        surveyOptions
      }

      val selected = if (options.sizeIs == 1) {
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
      val options = if (lastUpdatedOptions.sizeIs != 1) {
        Seq(ServerFilterOption("all", lastUpdatedOptions.map(_.count).sum)) ++ lastUpdatedOptions
      }
      else {
        lastUpdatedOptions
      }

      val selected = if (options.sizeIs == 1) {
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

  private def surveyPipeline(parameters: LocationRoutesParameters): MongoPipeline = {
    LocationQuery.surveyPipeline(
      surveyDateInfo,
      Seq(
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  private def lastUpdatedPipeline(parameters: LocationRoutesParameters): MongoPipeline = {
    LocationQuery.lastUpdatedPipeline(
      surveyDateInfo,
      Seq(
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  private def proposedPipeline(parameters: LocationRoutesParameters): MongoPipeline = {
    LocationQuery.proposedPipeline(
      Seq(
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      )
    )
  }

  private def factsPipeline(parameters: LocationRoutesParameters): MongoPipeline = {
    LocationQuery.factsPipeline(
      Seq(
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  private def factsTotalRouteCountPipeline(parameters: LocationRoutesParameters): MongoPipeline = {
    LocationQuery.countPipeline(
      Seq(
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  def countDocuments(subset: LocationSubset, parameters: LocationRoutesParameters): Long = {
    val filter = buildFilter(subset, parameters)
    val routeCount = database.routes.countFilteredDocuments(filter, log)
    log.debugElapsed {
      ("route count", routeCount)
    }
  }

  def find(
    subset: LocationSubset,
    parameters: LocationRoutesParameters
  ): Seq[LocationRouteInfo] = {

    val pipeline = Seq(
      filter(buildFilter(subset, parameters)),
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
      val docs = database.routes.aggregate(pipeline, classOf[LocationRouteInfoData]).zipWithIndex.map { case (doc, index) =>
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        val symbol = RouteSymbol.from(doc)
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

  private def mainFilters(subset: LocationSubset): MongoPipeline = {
    Seq(
      equal("active", true),
      equal("labels", Label.routeType(subset.routeType)),
      LocationQuery.locationFilter("labels", subset),
    )
  }

  private def buildFilter(subset: LocationSubset, parameters: LocationRoutesParameters): Bson = {
    val filters: MongoPipeline = mainFilters(subset) ++ Seq(
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed)
    ).flatten
    and(filters: _*)
  }
}
