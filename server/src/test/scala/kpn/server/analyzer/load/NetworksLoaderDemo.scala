package kpn.server.analyzer.load

import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

import kpn.api.custom.Timestamp
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContext
import kpn.core.db.couch.Couch
import kpn.core.db.couch.CouchConfig
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.tools.config.AnalysisRepositoryConfiguration
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.json.Json
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.BlackListRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

object NetworksLoaderDemo {

  def main(args: Array[String]): Unit = {

    val log = Log(classOf[NetworksLoaderDemo])
    val executor = buildExecutor()
    try {
      new NetworksLoaderDemo(executor).run()
    }
    catch {
      case e: Exception => log.error("aborted", e)
    }
    finally {
      executor.shutdown()
      log.info("Done")
    }
  }

  private def buildExecutor(): ThreadPoolTaskExecutor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(4)
    executor.setMaxPoolSize(4)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setThreadNamePrefix("kpn-")
    executor.initialize()
    executor
  }
}

/**
 * Try out loading of networks with different settings for parallelization.
 */
class NetworksLoaderDemo(analysisExecutor: Executor) {

  private val log = Log(classOf[NetworksLoaderDemo])

  private val couchConfig: CouchConfig = Couch.config
  private val database = new DatabaseImpl(DatabaseContext(couchConfig, Json.objectMapper, "master5"))
  private val analysisRepository: AnalysisRepository = new AnalysisRepositoryConfiguration(database).analysisRepository
  private val overpassQueryExecutor = new OverpassQueryExecutorImpl()
  private val analysisContext = new AnalysisContext()
  private val networkIdsLoader = new NetworkIdsLoaderImpl(overpassQueryExecutor)
  private val networkLoader = new NetworkLoaderImpl(overpassQueryExecutor)
  private val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
  private val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
  private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)
  private val tileCalculator = new TileCalculatorImpl()
  private val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
  private val locationConfiguration = new LocationConfigurationReader().read()
  private val routeLocator = new RouteLocatorImpl(locationConfiguration)
  private val routeRepository = new RouteRepositoryImpl(database)
  private val routeLocationAnalyzer = new RouteLocationAnalyzerImpl(routeRepository, routeLocator)
  private val routeAnalyzer = new MasterRouteAnalyzerImpl(
    analysisContext,
    routeLocationAnalyzer,
    new AccessibilityAnalyzerImpl(),
    routeTileAnalyzer
  )
  private val networkAnalyzer = new NetworkAnalyzerImpl(analysisContext, relationAnalyzer, countryAnalyzer, null /*TODO LOC*/, null /*TODO LOC*/)
  private val blackListRepository = new BlackListRepositoryImpl(database)

  private val networkInitialLoaderWorker: NetworkInitialLoaderWorker = new NetworkInitialLoaderWorkerImpl(
    analysisContext,
    analysisRepository,
    networkLoader,
    networkRelationAnalyzer,
    networkAnalyzer,
    blackListRepository
  )

  private val networkInitialLoader: NetworkInitialLoader = new NetworkInitialLoaderImpl(
    analysisExecutor,
    networkInitialLoaderWorker
  )

  val networksLoader = new NetworksLoaderImpl(
    networkIdsLoader,
    networkInitialLoader
  )

  def run(): Unit = {
    log.unitElapsed {
      networksLoader.load(Timestamp(2016, 9, 1))
      "networksLoader"
    }
  }
}
