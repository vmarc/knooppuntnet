package kpn.database.actions.locations

import kpn.api.common.SurveyDateInfo
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.changes.filter.ServerFilterOption
import kpn.api.common.location.BooleanParameter
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodeOptions
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.NetworkScope
import kpn.api.custom.ScopedNetworkType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Facet
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryLocationNodes(database: Database, surveyDateInfo: SurveyDateInfo) {
  private val log = Log(classOf[MongoQueryLocationNodes])

  def filterOptions(subset: LocationSubset, parameters: LocationNodesParameters): LocationNodeOptions = {
    val pipeline = Seq(
      filter(and(mainFilters(subset): _*)),
      facet(
        Facet("factsTotalRouteCount", factsTotalRouteCountPipeline(subset, parameters): _*),
        Facet("facts", factsPipeline(subset, parameters): _*),
        Facet("proposed", proposedPipeline(subset, parameters): _*),
        Facet("survey", surveyPipeline(subset, parameters): _*),
        Facet("lastUpdated", lastUpdatedPipeline(subset, parameters): _*),
        Facet("integrityCheckCount", integrityCheckPipeline(subset, parameters): _*),
        Facet("integrityCheckTotalRouteCount", integrityCheckTotalRouteCountPipeline(subset, parameters): _*),
        Facet("integrityCheckFailedCount", integrityCheckFailedPipeline(subset, parameters): _*),
        Facet("integrityCheckFailedTotalRouteCount", integrityCheckFailedTotalRouteCountPipeline(subset, parameters): _*),
      )
    )

    println(Mongo.pipelineString(pipeline))

    val groups = database.nodes.aggregate[Groups](pipeline)

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

    val fact = {
      val totalCount = groups.flatMap(_.factsTotalRouteCount).map(_.count).sum
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

    val integrityCheck = {
      val all = groups.flatMap(_.integrityCheckTotalRouteCount).map(_.count).sum
      val yes = groups.flatMap(_.integrityCheckCount).map(_.count).sum
      val options = if (yes == 0 || yes == all) {
        Seq(ServerFilterOption("all", all))
      }
      else {
        val no = all - yes
        Seq(
          ServerFilterOption("all", all),
          ServerFilterOption(BooleanParameter.yes.entryName, yes),
          ServerFilterOption(BooleanParameter.no.entryName, no),
        )
      }

      val selected = if (options.size == 1) {
        options.head.name
      }
      else {
        parameters.proposed match {
          case None => "all"
          case Some(proposed) =>
            options.find(_.name == proposed.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    val integrityCheckFailed = {
      val all = groups.flatMap(_.integrityCheckFailedTotalRouteCount).map(_.count).sum
      val yes = groups.flatMap(_.integrityCheckFailedCount).map(_.count).sum

      val options = if (yes == 0 || yes == all) {
        Seq(ServerFilterOption("all", all))
      }
      else {
        Seq(
          ServerFilterOption("all", all),
          ServerFilterOption(BooleanParameter.yes.entryName, yes),
          ServerFilterOption(BooleanParameter.no.entryName, all - yes),
        )
      }

      val selected = if (options.size == 1) {
        options.head.name
      }
      else {
        parameters.proposed match {
          case None => "all"
          case Some(proposed) =>
            options.find(_.name == proposed.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    LocationNodeOptions(
      integrityCheck,
      integrityCheckFailed,
      fact,
      survey,
      lastUpdated,
      proposed
    )
  }

  def countDocuments(subset: LocationSubset, parameters: LocationNodesParameters): Long = {
    val filter = buildFilter(subset, parameters)
    database.nodes.countDocuments(filter, log)
  }

  def find(
    subset: LocationSubset,
    parameters: LocationNodesParameters,
  ): Seq[LocationNodeInfo] = {

    val pipeline = Seq(
      filter(buildFilter(subset, parameters)),
      sort(orderBy(ascending("names.name", "_id"))),
      skip(parameters.pageSize.toInt * parameters.pageIndex.toInt),
      limit(parameters.pageSize.toInt),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          include("name"),
          include("names"),
          include("latitude"),
          include("longitude"),
          include("lastUpdated"),
          include("lastSurvey"),
          include("tags"),
          include("facts"),
          include("routeReferences"),
        )
      )
    )

    log.debugElapsed {
      val locationNodeInfoDocs = database.nodes.aggregate[LocationNodeInfoDoc](pipeline)
      val locationNodeInfos = locationNodeInfoDocs.zipWithIndex.map { case (doc, index) =>
        val tagValues = NetworkScope.all.map(scope => ScopedNetworkType(scope, subset.networkType)).map(_.expectedRouteRelationsTag).flatMap { tagKey =>
          doc.tags(tagKey)
        }
        val expectedRouteCount = tagValues.headOption.getOrElse("-")
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        LocationNodeInfo(
          rowIndex,
          doc.id,
          doc.networkTypeName(subset.networkType),
          doc.networkTypeLongName(subset.networkType).getOrElse("-"),
          doc.latitude,
          doc.longitude,
          doc.lastUpdated,
          doc.lastSurvey,
          doc.facts,
          expectedRouteCount,
          doc.routeReferences.filter(_.networkType == subset.networkType)
        )
      }
      (s"location nodes: ${locationNodeInfos.size}", locationNodeInfos)
    }
  }

  private def mainFilters(subset: LocationSubset): Seq[Bson] = {
    Seq(
      equal("labels", Label.active),
      equal("labels", Label.networkType(subset.networkType)),
      LocationQuery.locationFilter("labels", subset),
    )
  }

  private def buildFilter(subset: LocationSubset, parameters: LocationNodesParameters): Bson = {
    val filters: Seq[Bson] = mainFilters(subset) ++ Seq(
      LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
      LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed)
    ).flatten
    and(filters: _*)
  }

  private def surveyPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.surveyPipeline(
      surveyDateInfo,
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  private def lastUpdatedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.lastUpdatedPipeline(
      surveyDateInfo,
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  private def proposedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.proposedPipeline(
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      )
    )
  }

  private def factsPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.factsPipeline(
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  private def factsTotalRouteCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.routeCountPipeline(
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed)
      )
    )
  }

  private def integrityCheckPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.integrityCheckPipeline(subset, integrityCheckOtherFilters(subset, parameters))
  }

  private def integrityCheckTotalRouteCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.routeCountPipeline(integrityCheckOtherFilters(subset, parameters))
  }

  private def integrityCheckOtherFilters(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Option[Bson]] = {
    Seq(
      LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed)
    )
  }

  private def integrityCheckFailedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.integrityCheckFailedPipeline(subset, integrityCheckFailedOtherFilters(subset, parameters))
  }

  private def integrityCheckFailedTotalRouteCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.routeCountPipeline(integrityCheckFailedOtherFilters(subset, parameters))
  }

  private def integrityCheckFailedOtherFilters(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Option[Bson]] = {
    Seq(
      LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed)
    )
  }
}
