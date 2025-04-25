package kpn.core.tools.analysis

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.MetaData
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Relation
import kpn.core.analysis.Facts
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class AnalysisStartRouteAnalyzer(log: Log, config: AnalysisStartConfiguration)(implicit val executionContext: ExecutionContext) {

  def analyze(): Unit = {
    Log.context("route-analysis") {
      log.infoElapsed {
        val overpassRouteIds = collectOverpassRouteIds()
        val databaseRouteIds = config.routeRepository.allRouteIds()
        val routeIds = (overpassRouteIds.toSet -- databaseRouteIds.toSet).toSeq.sorted
        val analyzedRouteIds = analyzeRoutes(routeIds)
        (s"completed (${analyzedRouteIds.size} routes", ())
      }
    }
  }

  private def collectOverpassRouteIds(): Seq[Long] = {
    log.info(s"Collecting overpass route ids")
    log.infoElapsed {
      val ids = config.overpassRepository.oldRouteIds(config.timestamp)
      (s"${ids.size} overpass route ids", ids)
    }
  }

  private def analyzeRoutes(routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val batchSize = 100
      val futures = Future.sequence {
        val routeIdsSize = routeIds.size
        routeIds.sliding(batchSize, batchSize).zipWithIndex.map { case (batchRouteIds, index) =>
          Future(
            Log.context(s"${index * batchSize}/$routeIdsSize") {
              analyzeRouteBatch(batchRouteIds)
            }
          )
        }.toSeq
      }
      val updatedRouteIds = Await.result(futures, Duration(3, TimeUnit.HOURS)).flatten
      (s"${updatedRouteIds.size} routes analyzed", updatedRouteIds)
    }
  }

  private def analyzeRouteBatch(routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      routeIds.foreach { routeId =>
        config.overpassRepository.relationTopLevel(config.timestamp, routeId) match {
          case Some(relation) =>
            val hierarchy = if (relation.relationIdMembers.nonEmpty) {
              config.overpassRepository.relationHierarchy(config.timestamp, routeId)
            }
            else {
              None
            }
            analyzeRoute(relation, hierarchy)
          case None =>
        }
      }
      (s"processed ${routeIds.size} routes: ${routeIds.mkString(", ")}", routeIds)
    }
  }

  private def analyzeRoute(relation: Relation, hierarchy: Option[RouteRelation]): Unit = {
    Log.context(s"route=${relation.id}") {
      try {
        val context = config.baseRouteMainAnalyzer.analyze(relation, hierarchy)
        if (!context.abort) {
          val baseRouteDoc = new BaseRouteDocBuilder(context).build()
          config.routeRepository.saveBaseRoute(baseRouteDoc)
          // TODO redesign - move to phase 2
          config.routeMainAnalyzer.analyze(baseRouteDoc) match {
            case None =>
            case Some(routeDoc) =>
              config.routeRepository.saveRoute(routeDoc)
              saveRouteChange(routeDoc)
          }
        }
      } catch {
        case e: Exception =>
          log.error(s"Error processing route ${relation.id}", e)
          throw e
      }
    }
  }

  private def saveRouteChange(routeDoc: RouteDoc): Unit = {

    val key = config.changeSetContext.buildChangeKey(routeDoc.id)
    val facts = routeDoc.facts
    val locationFacts = facts.filter(Facts.locationFacts.contains)
    val routeData = RouteData(
      routeDoc.summary.id,
      MetaData(routeDoc.version, routeDoc.lastUpdated, routeDoc.changeSetId),
      routeDoc.summary.countries.toSeq,
      routeDoc.summary.routeTypes,
      routeDoc.summary.name: String,
      routeDoc.nodes.nodes,
      Seq.empty, // TODO redesign - ways ???
      routeDoc.facts,
      routeDoc.summary.meters,
      routeDoc.locationAnalysis,
      routeDoc.summary.tags
    )

    config.changeSetRepository.saveRouteChange(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        name = routeDoc.summary.name,
        locationAnalysis = routeDoc.locationAnalysis,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        before = None,
        after = Some(routeData),
        removedWays = Seq.empty,
        addedWays = Seq.empty,
        updatedWays = Seq.empty,
        diffs = RouteDiff(factDiffs = Some(FactDiffs(remaining = facts))),
        facts = routeDoc.facts,
        investigate = facts.nonEmpty,
        impact = true,
        locationInvestigate = locationFacts.nonEmpty,
        locationImpact = true,
      )
    )
  }
}
