package kpn.core.tools.analysis

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.ChangeType
import kpn.api.custom.Fact
import kpn.api.custom.Relation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis

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
      val ids = config.overpassRepository.routeIds(config.timestamp)
      (s"${ids.size} overpass route ids", ids)
    }
  }

  private def analyzeRoutes(routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val batchSize = 100
      val futures = Future.sequence(
        routeIds.sliding(batchSize, batchSize).zipWithIndex.map { case (batchRouteIds, index) =>
          Future(
            Log.context(s"${index * batchSize}/${routeIds.size}") {
              analyzeRouteBatch(batchRouteIds)
            }
          )
        }.toSeq
      )
      val updatedRouteIds = Await.result(futures, Duration(3, TimeUnit.HOURS)).flatten
      (s"${updatedRouteIds.size} routes analyzed", updatedRouteIds)
    }
  }

  private def analyzeRouteBatch(routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val relations = config.overpassRepository.fullRelations(config.timestamp, routeIds)
      relations.foreach(analyzeRoute)
      val ids = relations.map(_.id)
      (s"processed ${ids.size} routes: ${ids.mkString(", ")}", ids)
    }
  }

  private def analyzeRoute(relation: Relation): Unit = {
    Log.context(s"route=${relation.id}") {
      try {
        config.masterRouteAnalyzer.analyze(relation) match {
          case None =>
          case Some(routeAnalysis) =>
            config.routeRepository.save(routeAnalysis.route)
            saveRouteChange(routeAnalysis)
        }
      } catch {
        case e: Exception =>
          log.error(s"Error processing route ${relation.id}", e)
          throw e
      }
    }
  }

  private def saveRouteChange(routeAnalysis: RouteAnalysis): Unit = {

    val key = config.changeSetContext.buildChangeKey(routeAnalysis.route.id)
    val facts = routeAnalysis.route.facts.toSet
    val locationFacts = facts.filter(Fact.locationFacts.contains)

    config.changeSetRepository.saveRouteChange(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        name = routeAnalysis.route.summary.name,
        locationAnalysis = routeAnalysis.route.analysis.locationAnalysis,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        before = None,
        after = Some(routeAnalysis.toRouteData),
        removedWays = Seq.empty,
        addedWays = Seq.empty,
        updatedWays = Seq.empty,
        diffs = RouteDiff(factDiffs = Some(FactDiffs(remaining = facts))),
        facts = routeAnalysis.route.facts,
        Seq.empty,
        investigate = facts.nonEmpty,
        impact = true,
        locationInvestigate = locationFacts.nonEmpty,
        locationImpact = true
      )
    )
  }
}
