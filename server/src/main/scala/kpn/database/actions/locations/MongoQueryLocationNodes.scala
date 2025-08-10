package kpn.database.actions.locations

import com.mongodb.client.model.Aggregates.count
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
import kpn.api.common.RouteScope
import kpn.api.common.SurveyDateInfo
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.changes.filter.ServerFilterOption
import kpn.api.common.location.BooleanParameter
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodeOptions
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.ScopedRouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.ffacet
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.bson.conversions.Bson

class MongoQueryLocationNodes(database: Database, surveyDateInfo: SurveyDateInfo) {
  private val log = Log(classOf[MongoQueryLocationNodes])

  def filterOptions(subset: LocationSubset, parameters: LocationNodesParameters): LocationNodeOptions = {
    val pipeline = Seq(filter(and(subsetFilter(subset): _*))) ++ Seq(
      facet(
        ffacet("factsTotalNodeCount", factsTotalNodeCountPipeline(subset, parameters)),
        ffacet("facts", factsPipeline(subset, parameters)),
        ffacet("proposed", proposedPipeline(subset, parameters)),
        ffacet("survey", surveyPipeline(subset, parameters)),
        ffacet("lastUpdated", lastUpdatedPipeline(subset, parameters)),
        ffacet("integrityCheckCount", integrityCheckPipeline(subset, parameters)),
        ffacet("integrityCheckTotalNodeCount", integrityCheckTotalNodeCountPipeline(subset, parameters)),
        ffacet("integrityCheckFailedCount", integrityCheckFailedPipeline(subset, parameters)),
        ffacet("integrityCheckFailedTotalNodeCount", integrityCheckFailedTotalNodeCountPipeline(subset, parameters)),
        ffacet("referencedInRoutesCount", referencedInRoutesCountPipeline(subset, parameters)),
        ffacet("referencedInRoutesTotalNodeCount", referencedInRoutesTotalNodeCountPipeline(subset, parameters)),
        ffacet("totalNodeCount", totalNodeCountPipeline(subset, parameters)),
      )
    )

    val groups = database.nodes.aggregate(pipeline, classOf[NodeFilterOptionQueryResult])

    val proposed = {
      val options = groups.flatMap(_.proposed).flatMap(_.options)
      val oo = if (options.sizeIs != 1) {
        Seq(ServerFilterOption("all", options.map(_.count).sum)) ++ options
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
          case Some(proposedValue) =>
            oo.find(_.name == proposedValue.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, oo)
    }

    val fact = {
      val totalCount = groups.flatMap(_.factsTotalNodeCount).map(_.count).sum
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
          case Some(surveyValue) =>
            surveyOptions.find(_.name == surveyValue.entryName) match {
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
          case Some(lastUpdatedValue) =>
            options.find(_.name == lastUpdatedValue.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    val integrityCheck = {
      val all = groups.flatMap(_.integrityCheckTotalNodeCount).map(_.count).sum
      val yes = groups.flatMap(_.integrityCheckCount).map(_.count).sum
      val options = if (yes == 0 || yes == all) {
        Seq(ServerFilterOption("all", all))
      }
      else {
        val no = all - yes
        Seq(
          ServerFilterOption("all", all),
          ServerFilterOption(BooleanParameter.Yes.entryName, yes),
          ServerFilterOption(BooleanParameter.No.entryName, no),
        )
      }

      val selected = if (options.sizeIs == 1) {
        options.head.name
      }
      else {
        parameters.proposed match {
          case None => "all"
          case Some(proposedValue) =>
            options.find(_.name == proposedValue.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    val integrityCheckFailed = {
      val all = groups.flatMap(_.integrityCheckFailedTotalNodeCount).map(_.count).sum
      val yes = groups.flatMap(_.integrityCheckFailedCount).map(_.count).sum

      val options = if (yes == 0 || yes == all) {
        Seq(ServerFilterOption("all", all))
      }
      else {
        Seq(
          ServerFilterOption("all", all),
          ServerFilterOption(BooleanParameter.Yes.entryName, yes),
          ServerFilterOption(BooleanParameter.No.entryName, all - yes),
        )
      }

      val selected = if (options.sizeIs == 1) {
        options.head.name
      }
      else {
        parameters.proposed match {
          case None => "all"
          case Some(proposedValue) =>
            options.find(_.name == proposedValue.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    val referencedInRoutes = {
      val all = groups.flatMap(_.referencedInRoutesTotalNodeCount).map(_.count).sum
      val yes = groups.flatMap(_.referencedInRoutesCount).map(_.count).sum

      val options = if (yes == 0 || yes == all) {
        Seq(ServerFilterOption("all", all))
      }
      else {
        Seq(
          ServerFilterOption("all", all),
          ServerFilterOption(BooleanParameter.Yes.entryName, yes),
          ServerFilterOption(BooleanParameter.No.entryName, all - yes),
        )
      }

      val selected = if (options.sizeIs == 1) {
        options.head.name
      }
      else {
        parameters.proposed match {
          case None => "all"
          case Some(proposedValue) =>
            options.find(_.name == proposedValue.entryName) match {
              case None => "all"
              case Some(value) => value.name
            }
        }
      }
      ServerFilterGroup(selected, options)
    }

    val totalNodeCount = groups.flatMap(_.totalNodeCount).map(_.count).sum

    LocationNodeOptions(
      integrityCheck,
      integrityCheckFailed,
      fact,
      survey,
      lastUpdated,
      proposed,
      referencedInRoutes,
      totalNodeCount
    )
  }

  def countDocuments(subset: LocationSubset, parameters: LocationNodesParameters): Long = {
    val pipeline = Seq(filter(nodeFilter(subset, parameters))) ++ Seq(count())
    log.debugElapsed {
      val result = database.nodes.aggregate(pipeline, classOf[CountResult], log).map(_.count).sum
      ("node count", result)
    }
  }

  def find(
    subset: LocationSubset,
    parameters: LocationNodesParameters,
  ): Seq[LocationNodeInfo] = {

    val pipeline = Seq(filter(nodeFilter(subset, parameters))) ++ Seq(
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
      val locationNodeInfoDocs = database.nodes.aggregate(pipeline, classOf[LocationNodeInfoDoc], log)
      val locationNodeInfos = locationNodeInfoDocs.zipWithIndex.map { case (doc, index) =>
        val tagValues = RouteScope.all.map(scope => ScopedRouteType(subset.routeType, scope)).map(_.expectedRouteRelationsTag).flatMap { tagKey =>
          doc.tagValue(tagKey)
        }
        val expectedNodeCount = tagValues.headOption.getOrElse("-")
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        LocationNodeInfo(
          rowIndex,
          doc.id,
          doc.routeTypeName(subset.routeType),
          doc.routeTypeLongName(subset.routeType).getOrElse("-"),
          doc.latitude,
          doc.longitude,
          doc.lastUpdated,
          doc.lastSurvey,
          doc.facts,
          expectedNodeCount,
          doc.routeReferences.filter(_.routeType == subset.routeType)
        )
      }
      (s"location nodes: ${locationNodeInfos.size}", locationNodeInfos)
    }
  }

  private def subsetFilter(subset: LocationSubset): MongoPipeline = {
    Seq(
      equal("active", true),
      equal("labels", Label.routeType(subset.routeType)),
      LocationQuery.locationFilter("labels", subset),
    )
  }

  private def nodeFilter(subset: LocationSubset, parameters: LocationNodesParameters): Bson = {
    val filters: MongoPipeline = subsetFilter(subset) ++ allFilters(subset, parameters).flatten
    and(filters: _*)
  }

  private def allFilters(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Option[Bson]] = {
    Seq(
      LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
      LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed),
      LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
    )
  }

  private def surveyPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.surveyPipeline(
      surveyDateInfo,
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed),
        LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
      )
    )
  }

  private def lastUpdatedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.lastUpdatedPipeline(
      surveyDateInfo,
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.proposedFilter(parameters.proposed),
        LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
      )
    )
  }

  private def proposedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.proposedPipeline(
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.factFilter(parameters.fact),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
      )
    )
  }

  private def factsPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.factsPipeline(
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed),
        LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
      )
    )
  }

  private def factsTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.countPipeline(
      Seq(
        LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
        LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
        LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
        LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
        LocationQuery.proposedFilter(parameters.proposed),
        LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
      )
    )
  }

  private def integrityCheckPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.integrityCheckPipeline(subset, integrityCheckOtherFilters(subset, parameters))
  }

  private def integrityCheckTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.countPipeline(integrityCheckOtherFilters(subset, parameters))
  }

  private def referencedInRoutesCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.referencedInRoutesPipeline(subset, referencedInRoutesOtherFilters(subset, parameters))
  }

  private def referencedInRoutesTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.countPipeline(referencedInRoutesOtherFilters(subset, parameters))
  }

  private def totalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.countPipeline(allFilters(subset, parameters))
  }

  private def referencedInRoutesOtherFilters(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Option[Bson]] = {
    Seq(
      LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
      LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed)
    )
  }

  private def integrityCheckOtherFilters(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Option[Bson]] = {
    Seq(
      LocationQuery.integrityCheckFailedFilter(subset, parameters.integrityCheckFailed),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed),
      LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
    )
  }

  private def integrityCheckFailedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.integrityCheckFailedPipeline(subset, integrityCheckFailedOtherFilters(subset, parameters))
  }

  private def integrityCheckFailedTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): MongoPipeline = {
    LocationQuery.countPipeline(integrityCheckFailedOtherFilters(subset, parameters))
  }

  private def integrityCheckFailedOtherFilters(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Option[Bson]] = {
    Seq(
      LocationQuery.integrityCheckFilter(subset, parameters.integrityCheck),
      LocationQuery.factFilter(parameters.fact),
      LocationQuery.surveyFilter(surveyDateInfo, parameters.survey),
      LocationQuery.lastUpdatedFilter(surveyDateInfo, parameters.lastUpdated),
      LocationQuery.proposedFilter(parameters.proposed),
      LocationQuery.referencedInRoutesFilter(subset, parameters.referencedInRoutes),
    )
  }
}
