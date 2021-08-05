package kpn.server.analyzer.full.route

import kpn.api.common.route.RouteInfo
import kpn.api.custom.Timestamp
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.analyzer.load.RouteLoader
import kpn.server.analyzer.load.RouteLoaderImpl
import kpn.server.analyzer.load.orphan.route.RouteIdsLoader
import kpn.server.analyzer.load.orphan.route.RouteIdsLoaderImpl
import kpn.server.repository.RouteRepositoryImpl
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.ReplaceOneModel
import org.mongodb.scala.model.ReplaceOptions

import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

object FullRouteAnalyzerImpl {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val executionContext = ExecutionContext.fromExecutor(
        // TODO use ServerConfiguration.analysisExecutor with given pool size and name
        Executors.newFixedThreadPool(9)
      )

      val executor = new OverpassQueryExecutorRemoteImpl()
      val cachingOverpassQueryExecutor = new CachingOverpassQueryExecutor(
        new File("/kpn/cache"),
        executor
      )
      val analysisContext = new AnalysisContext()
      val routeIdsLoader = new RouteIdsLoaderImpl(cachingOverpassQueryExecutor)
      val routeLoader = new RouteLoaderImpl(executor)
      val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
      val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
      val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
      val locationConfiguration = new LocationConfigurationReader().read()
      val routeRepository = new RouteRepositoryImpl(database, null, mongoEnabled = true)
      val routeLocator = new RouteLocatorImpl(locationConfiguration)
      val routeLocationAnalyzer = new RouteLocationAnalyzerImpl(
        routeRepository,
        routeLocator
      )
      val tileCalculator = new TileCalculatorImpl()
      val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
      val routeTileAnalyzer = new RouteTileAnalyzer(
        routeTileCalculator
      )
      val routeAnalyzer = new MasterRouteAnalyzerImpl(
        analysisContext,
        routeCountryAnalyzer,
        routeLocationAnalyzer,
        routeTileAnalyzer
      )

      val analyzer = new FullRouteAnalyzerImpl(
        database,
        routeIdsLoader,
        routeLoader,
        routeAnalyzer,
        executionContext
      )
      val context = FullAnalysisContext(Timestamp(2021, 8, 2, 0, 0, 0))
      val contextAfter = analyzer.analyze(context)
    }
  }
}

class FullRouteAnalyzerImpl(
  database: Database,
  routeIdsLoader: RouteIdsLoader,
  routeLoader: RouteLoader,
  masterRouteAnalyzer: MasterRouteAnalyzer,
  implicit val executionContext: ExecutionContext
) extends FullRouteAnalyzer {

  private val log = Log(classOf[FullRouteAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {

    // val existingRouteIds = findActiveRouteIds()

    val allRouteIds = collectRouteIds(context.timestamp).drop(5000).take(5000)

    val batchSize = 100
    val updateFutures = allRouteIds.sliding(batchSize, batchSize).zipWithIndex.map { case (routeIds, index) =>
      Future(
        Log.context(s"${index * batchSize}/${allRouteIds.size}") {


          val relations = routeLoader.load(context.timestamp, routeIds)
          val routeInfos = relations.map { relation =>

            Log.context(s"route=${relation.id}") {
              try {
                masterRouteAnalyzer.analyze(relation).route
              } catch {
                case e: Exception =>
                  log.error(s"Error processing route ${relation.id}", e)
                  throw e
              }
            }
          }
          log.infoElapsed("save") {
            val requests = routeInfos.map { doc =>
              ReplaceOneModel[RouteInfo](Filters.equal("_id", doc._id), doc, ReplaceOptions().upsert(true))
            }
            val future = database.routes.native.bulkWrite(requests).toFuture()
            Await.result(future, Duration(5, TimeUnit.MINUTES))
          }
          relations.map(_.id)
        }
      )
    }.toSeq

    val loadIdFuturesSeq = Future.sequence(updateFutures)

    val updateResult = Await.result(loadIdFuturesSeq, Duration(60, TimeUnit.MINUTES))
    log.info(s"routeIds save: ${updateResult.flatten}")

    // val obsoleteNodeIds = (existingNodeIds.toSet -- nodeIds).toSeq.sorted
    // deactivateObsoleteNodes(obsoleteNodeIds)

    context
  }

  private def collectRouteIds(timestamp: Timestamp): Seq[Long] = {
    log.info(s"Collecting all route ids")
    log.elapsed {
      val ids = routeIdsLoader.load(timestamp).toSeq.sorted
      (s"${ids.size} route ids loaded", ids)
    }
  }

}
