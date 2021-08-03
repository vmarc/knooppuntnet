package kpn.core.tools.support

import java.time.LocalDate
import java.util.concurrent.CompletableFuture.allOf
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

import kpn.api.custom.Country
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.RouteLoader
import kpn.server.analyzer.load.RouteLoaderImpl
import kpn.server.analyzer.load.orphan.route.RouteIdsLoader
import kpn.server.analyzer.load.orphan.route.RouteIdsLoaderImpl
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import scala.annotation.tailrec

object MapChartTool {

  private val poolSize = 10
  private val log = Log(classOf[MapChartTool])

  private val start = LocalDate.parse("2020-01-01")
  private val end = LocalDate.parse("2020-11-11")

  def main(args: Array[String]): Unit = {

    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
    val overpassQueryExecutor = new OverpassQueryExecutorImpl()
    val routeIdsLoader = new RouteIdsLoaderImpl(overpassQueryExecutor)
    val routeLoader = new RouteLoaderImpl(overpassQueryExecutor, countryAnalyzer)
    val routeLoaderExecutor = buildExecutor()

    new MapChartTool(routeIdsLoader, routeLoader, routeLoaderExecutor).analyze()
  }

  private def buildExecutor(): Executor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(poolSize)
    executor.setMaxPoolSize(poolSize)
    executor.setKeepAliveSeconds(0)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setWaitForTasksToCompleteOnShutdown(true)
    executor.setAwaitTerminationSeconds(60 * 5)
    executor.setThreadNamePrefix("pool-")
    executor.initialize()
    executor
  }

}

import kpn.core.tools.support.MapChartTool._

class MapChartTool(
  routeIdsLoader: RouteIdsLoader,
  routeLoader: RouteLoader,
  routeLoaderExecutor: Executor
) {

  def analyze(): Unit = {
    analyzeDay(end)
    log.info("Done")
  }

  @tailrec
  private def analyzeDay(day: LocalDate): Unit = {
    if (!day.isBefore(start)) {
      analyze(toTimestamp(day))
      analyzeDay(day.minusDays(7))
    }
  }

  private def analyze(timestamp: Timestamp): Unit = {
    Log.context(timestamp.yyyymmdd) {
      log.info(s"collecting routeIds")
      val routeIds = routeIdsLoader.loadByType(timestamp, ScopedNetworkType.rwn).toSeq.sorted
      val nlRouteCount = countNlRoutes(timestamp, routeIds)
      log.warn(s"NL hiking routes: $nlRouteCount")
      println(s"${timestamp.yyyymmdd} $nlRouteCount")
    }
  }

  private def countNlRoutes(timestamp: Timestamp, routeIds: Seq[Long]): Int = {
    if (routeIds.isEmpty) {
      0
    }
    else {
      log.debugElapsed {
        val futures = routeIds.zipWithIndex.map { case (routeId, index) =>
          val context = Log.contextAnd(s"${index + 1}/${routeIds.size}, route=$routeId")
          supplyAsync(() => Log.context(context)(isNlRoute(timestamp, routeId)), routeLoaderExecutor).exceptionally { ex =>
            val message = "Exception while loading route"
            Log.context(context) {
              log.error(message, ex)
            }
            false
          }
        }

        allOf(futures: _*).join()
        val isNlRoutes = futures.map(s => s.get())
        val routeCount = isNlRoutes.count(xx => xx)
        (s"Processed ${routeIds.size} routes: found $routeCount NL hiking routes", routeCount)
      }
    }
  }

  private def isNlRoute(timestamp: Timestamp, routeId: Long): Boolean = {
    routeLoader.loadRoute(timestamp, routeId) match {
      case None => false
      case Some(route) =>
        route.country match {
          case Some(Country.nl) => true
          case _ => false
        }
    }
  }

  private def toTimestamp(day: LocalDate): Timestamp = {
    Timestamp(day.getYear, day.getMonthValue, day.getDayOfMonth, 2, 0, 0)
  }

}
