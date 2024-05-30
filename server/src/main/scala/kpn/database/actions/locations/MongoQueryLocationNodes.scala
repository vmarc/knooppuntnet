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
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import kpn.server.analyzer.engine.analysis.location.ParcDuVercors
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.count
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Facet
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.geoIntersects
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Filters.not
import org.mongodb.scala.model.Filters.or
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

case class NodeFilterOptionQueryResult(
  factsTotalNodeCount: Seq[CountResult],
  facts: Seq[ServerFilterGroup],
  proposed: Seq[ServerFilterGroup],
  survey: Seq[ServerFilterGroup],
  lastUpdated: Seq[ServerFilterGroup],
  integrityCheckCount: Seq[CountResult],
  integrityCheckTotalNodeCount: Seq[CountResult],
  integrityCheckFailedCount: Seq[CountResult],
  integrityCheckFailedTotalNodeCount: Seq[CountResult],
  referencedInRoutesCount: Seq[CountResult],
  referencedInRoutesTotalNodeCount: Seq[CountResult],
  totalNodeCount: Seq[CountResult],
)

class MongoQueryLocationNodes(database: Database, surveyDateInfo: SurveyDateInfo) {
  private val log = Log(classOf[MongoQueryLocationNodes])

  def filterOptions(subset: LocationSubset, parameters: LocationNodesParameters): LocationNodeOptions = {
    val pipeline = Seq(filter(and(subsetFilter(subset): _*))) ++ boundaryFilter(subset, parameters) ++ Seq(
      facet(
        Facet("factsTotalNodeCount", factsTotalNodeCountPipeline(subset, parameters): _*),
        Facet("facts", factsPipeline(subset, parameters): _*),
        Facet("proposed", proposedPipeline(subset, parameters): _*),
        Facet("survey", surveyPipeline(subset, parameters): _*),
        Facet("lastUpdated", lastUpdatedPipeline(subset, parameters): _*),
        Facet("integrityCheckCount", integrityCheckPipeline(subset, parameters): _*),
        Facet("integrityCheckTotalNodeCount", integrityCheckTotalNodeCountPipeline(subset, parameters): _*),
        Facet("integrityCheckFailedCount", integrityCheckFailedPipeline(subset, parameters): _*),
        Facet("integrityCheckFailedTotalNodeCount", integrityCheckFailedTotalNodeCountPipeline(subset, parameters): _*),
        Facet("referencedInRoutesCount", referencedInRoutesCountPipeline(subset, parameters): _*),
        Facet("referencedInRoutesTotalNodeCount", referencedInRoutesTotalNodeCountPipeline(subset, parameters): _*),
        Facet("totalNodeCount", totalNodeCountPipeline(subset, parameters): _*),
      )
    )

    val groups = database.nodes.aggregate[NodeFilterOptionQueryResult](pipeline)

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
      val totalCount = groups.flatMap(_.factsTotalNodeCount).map(_.count).sum
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
      val all = groups.flatMap(_.integrityCheckTotalNodeCount).map(_.count).sum
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
      val all = groups.flatMap(_.integrityCheckFailedTotalNodeCount).map(_.count).sum
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

    val referencedInRoutes = {
      val all = groups.flatMap(_.referencedInRoutesTotalNodeCount).map(_.count).sum
      val yes = groups.flatMap(_.referencedInRoutesCount).map(_.count).sum

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
    val pipeline = Seq(filter(nodeFilter(subset, parameters))) ++ boundaryFilter(subset, parameters) ++ Seq(count())
    database.nodes.aggregate[CountResult](pipeline, log).map(_.count).sum
  }

  def find(
    subset: LocationSubset,
    parameters: LocationNodesParameters,
  ): Seq[LocationNodeInfo] = {

    val pipeline = Seq(filter(nodeFilter(subset, parameters))) ++ boundaryFilter(subset, parameters) ++ Seq(
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
        val expectedNodeCount = tagValues.headOption.getOrElse("-")
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
          expectedNodeCount,
          doc.routeReferences.filter(_.networkType == subset.networkType)
        )
      }
      (s"location nodes: ${locationNodeInfos.size}", locationNodeInfos)
    }
  }

  private def boundaryFilter(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    if (subset.name == ParcDuVercors.name) {
      Seq(
        filter(
          or(
            not(in("labels", ParcDuVercors.partialCommunes.map(l => Label.location(l)): _*)),
            and(
              in("labels", ParcDuVercors.partialCommunes.map(l => Label.location(l)): _*),
              geoIntersects("position", ParcDuVercors.boundaryBson)
            )
          )
        )
      )
    } else {
      Seq.empty
    }
  }

  private def subsetFilter(subset: LocationSubset): Seq[Bson] = {
    Seq(
      equal("labels", Label.active),
      equal("labels", Label.networkType(subset.networkType)),
      LocationQuery.locationFilter("labels", subset),
    )
  }

  private def nodeFilter(subset: LocationSubset, parameters: LocationNodesParameters): Bson = {
    val filters: Seq[Bson] = subsetFilter(subset) ++ allFilters(subset, parameters).flatten
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

  private def surveyPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
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

  private def lastUpdatedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
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

  private def proposedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
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

  private def factsPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
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

  private def factsTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
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

  private def integrityCheckPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.integrityCheckPipeline(subset, integrityCheckOtherFilters(subset, parameters))
  }

  private def integrityCheckTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.countPipeline(integrityCheckOtherFilters(subset, parameters))
  }

  private def referencedInRoutesCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.referencedInRoutesPipeline(subset, referencedInRoutesOtherFilters(subset, parameters))
  }

  private def referencedInRoutesTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.countPipeline(referencedInRoutesOtherFilters(subset, parameters))
  }

  private def totalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
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

  private def integrityCheckFailedPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
    LocationQuery.integrityCheckFailedPipeline(subset, integrityCheckFailedOtherFilters(subset, parameters))
  }

  private def integrityCheckFailedTotalNodeCountPipeline(subset: LocationSubset, parameters: LocationNodesParameters): Seq[Bson] = {
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
