package kpn.core.tools.analysis

import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
import kpn.server.analyzer.engine.analysis.network.base.BaseNetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.*
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.BaseNodeMainAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.*
import kpn.server.analyzer.engine.analysis.node.main.NodeMainAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.*
import kpn.server.analyzer.engine.analysis.post.*
import kpn.server.analyzer.engine.analysis.route.base.*
import kpn.server.analyzer.engine.analysis.route.base.analyzers.*
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.*
import kpn.server.analyzer.engine.tile.*
import kpn.server.analyzer.full.MainFullAnalyzer
import kpn.server.analyzer.full.analyzers.*
import kpn.server.overpass.OverpassRepository
import kpn.server.overpass.OverpassRepositoryImpl
import kpn.server.repository.*
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

class AnalysisConfiguration(databaseName: String) {

  // Database components
  private val mongoClient = Mongo.client
  private val database = Mongo.database(mongoClient, databaseName)
  val analysisRepository = new AnalysisRepositoryImpl(database)

  // Core repositories
  private val repositories = createRepositories()

  // Core analyzers and utilities
  private val locationAnalyzer = new LocationAnalyzerImpl(true, false)
  private val routeTileCache = new RouteTileCache()
  private val executor = createExecutor()

  // Public components for external use
  val singleBaseRouteAnalyzer: SingleBaseRouteAnalyzer = createSingleBaseRouteAnalyzer()
  val singleRouteAnalyzer: SingleRouteAnalyzer = createSingleRouteAnalyzer()
  val mainFullAnalyzer: MainFullAnalyzer = createMainFullAnalyzer()

  def shutdown(): Unit = {
    executor.shutdown()
    mongoClient.close()
  }

  // Repository factory methods
  private def createRepositories(): RepositoryGroup = {
    val networkRepository = new NetworkRepositoryImpl(database)
    val routeRepository = new RouteRepositoryImpl(database)
    val nodeRepository = new NodeRepositoryImpl(database)
    val changeSetRepository = new ChangeSetRepositoryImpl(database)
    val networkInfoRepository = new NetworkInfoRepositoryImpl(database)
    val rawDataRepository = new RawDataRepositoryDevelopmentImpl(database)
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val overpassRepository = new OverpassRepositoryImpl(overpassQueryExecutor)

    RepositoryGroup(
      networkRepository,
      routeRepository,
      nodeRepository,
      changeSetRepository,
      networkInfoRepository,
      rawDataRepository,
      overpassRepository
    )
  }

  // Create thread pool executor
  private def createExecutor(): ThreadPoolTaskExecutor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(6)
    executor.setMaxPoolSize(6)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setThreadNamePrefix("analyzer-start-")
    executor.initialize()
    executor
  }

  // Node analyzers
  private def createBaseNodeMainAnalyzer(): BaseNodeMainAnalyzer = {
    val baseNodeLocationAnalyzer = new BaseNodeLocationAnalyzer(locationAnalyzer)
    val countryAnalyzer = new BaseNodeCountryAnalyzer(locationAnalyzer)
    val nodeTileCalculator = new NodeTileCalculatorImpl(routeTileCache)
    val tileAnalyzer = new BaseNodeTileAnalyzer(nodeTileCalculator)

    new BaseNodeMainAnalyzer(
      countryAnalyzer,
      baseNodeLocationAnalyzer,
      tileAnalyzer
    )
  }

  private def createNodeMainAnalyzer(): NodeMainAnalyzer = {
    val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzer(repositories.nodeRepository)
    val nodeNetworkReferencesAnalyzer = new NodeNetworkReferencesAnalyzer(repositories.networkRepository)

    new NodeMainAnalyzer(
      nodeRouteReferencesAnalyzer,
      nodeNetworkReferencesAnalyzer
    )
  }

  // Route analyzers
  private def createBaseRouteMainAnalyzer(): BaseRouteMainAnalyzer = {
    val routeLocator = new RouteLocatorImpl(locationAnalyzer)
    val routeLocationAnalyzer = new BaseRouteLocationAnalyzerImpl(repositories.routeRepository, routeLocator)
    val routeCountryAnalyzer = new BaseRouteCountryAnalyzerImpl(locationAnalyzer, repositories.routeRepository)
    val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(routeTileCache)
    val routeTileAnalyzer = new BaseRouteTileAnalyzer(lineSegmentTileCalculator)

    new BaseRouteMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
  }

  private def createRouteMainAnalyzer(): RouteMainAnalyzer = {
    val routeRepo = repositories.routeRepository
    val routeBoundsAnalyzer = new RouteBoundsAnalyzer(routeRepo)
    val routeIdsAnalyzer = new RouteIdsAnalyzer(routeRepo)
    val routeSuperSegmentAnalyzer = new RouteSuperSegmentAnalyzer(routeRepo)
    val routeStructureRowsAnalyzer = new RouteStructureRowsAnalyzer(routeRepo)
    val routeParentAnalyzer = new RouteParentAnalyzer(routeRepo)
    val networkReferencesAnalyzer = new RouteNetworkReferencesAnalyzer(repositories.networkRepository)

    new RouteMainAnalyzer(
      routeBoundsAnalyzer,
      routeIdsAnalyzer,
      routeSuperSegmentAnalyzer,
      routeStructureRowsAnalyzer,
      routeParentAnalyzer,
      networkReferencesAnalyzer
    )
  }

  // Network analyzers
  private def createNetworkMainAnalyzer(): NetworkMainAnalyzer = {
    val networkInfoRouteAnalyzer = new NetworkRouteAnalyzer(repositories.routeRepository)
    val networkInfoNodeDocAnalyzer = new NetworkNodeDocAnalyzer(repositories.nodeRepository)
    val networkCountryAnalyzer = new NetworkCountryAnalyzer(locationAnalyzer)
    val networkInfoExtraAnalyzer = new NetworkExtraAnalyzer(repositories.overpassRepository)

    new NetworkMainAnalyzer(
      database,
      networkInfoRouteAnalyzer,
      networkInfoNodeDocAnalyzer,
      networkCountryAnalyzer,
      networkInfoExtraAnalyzer
    )
  }

  // Public component creators
  private def createSingleBaseRouteAnalyzer(): SingleBaseRouteAnalyzer = {
    val baseRouteDocBuilder = new BaseRouteDocBuilder()
    val baseRouteMainAnalyzer = createBaseRouteMainAnalyzer()

    new SingleBaseRouteAnalyzer(
      repositories.rawDataRepository,
      repositories.routeRepository,
      repositories.changeSetRepository,
      baseRouteMainAnalyzer,
      baseRouteDocBuilder
    )
  }

  private def createSingleRouteAnalyzer(): SingleRouteAnalyzer = {
    new SingleRouteAnalyzer(
      repositories.routeRepository,
      createRouteMainAnalyzer()
    )
  }

  private def createMainFullAnalyzer(): MainFullAnalyzer = {
    val fullAnalysisPipeline = createFullAnalysisPipeline()
    val postProcessor = createPostProcessor()

    new MainFullAnalyzer(
      fullAnalysisPipeline,
      postProcessor
    )
  }

  private def createFullAnalysisPipeline(): FullAnalysisPipeline = {
    val baseRouteDocBuilder = new BaseRouteDocBuilder()
    val baseNodeMainAnalyzer = createBaseNodeMainAnalyzer()
    val baseRouteMainAnalyzer = createBaseRouteMainAnalyzer()
    val baseNetworkMainAnalyzer = new BaseNetworkMainAnalyzer()
    val nodeMainAnalyzer = createNodeMainAnalyzer()
    val routeMainAnalyzer = createRouteMainAnalyzer()
    val networkMainAnalyzer = createNetworkMainAnalyzer()

    val fullBaseNodeAnalyzer = new FullBaseNodeAnalyzer(
      repositories.rawDataRepository,
      repositories.nodeRepository,
      baseNodeMainAnalyzer
    )

    val fullBaseRouteAnalyzer = new FullBaseRouteAnalyzer(
      repositories.rawDataRepository,
      repositories.routeRepository,
      baseRouteDocBuilder,
      singleBaseRouteAnalyzer
    )

    val fullBaseNetworkAnalyzer = new FullBaseNetworkAnalyzer(
      repositories.rawDataRepository,
      baseNetworkMainAnalyzer,
      repositories.networkRepository
    )

    val bulkNodeAnalyzer = new BulkNodeAnalyzer(
      repositories.rawDataRepository,
      nodeMainAnalyzer,
      repositories.nodeRepository
    )

    val initialNodeChangeBuilder = new InitialNodeChangeBuilder(
      repositories.changeSetRepository
    )

    val fullNodeAnalyzer = new FullNodeAnalyzer(
      repositories.rawDataRepository,
      repositories.nodeRepository,
      bulkNodeAnalyzer,
      initialNodeChangeBuilder
    )

    val initialRouteChangeBuilder = new InitialRouteChangeBuilder(
      repositories.changeSetRepository
    )

    val fullRouteAnalyzer = new FullRouteAnalyzer(
      repositories.routeRepository,
      routeMainAnalyzer,
      initialRouteChangeBuilder
    )

    val initialNetworkChangeBuilder = new InitialNetworkChangeBuilder(
      repositories.changeSetRepository,
      repositories.networkInfoRepository
    )

    val fullNetworkAnalyzer = new FullNetworkAnalyzer(
      repositories.networkRepository,
      networkMainAnalyzer,
      initialNetworkChangeBuilder
    )

    new FullAnalysisPipeline(
      fullBaseNodeAnalyzer,
      fullBaseRouteAnalyzer,
      fullBaseNetworkAnalyzer,
      fullNodeAnalyzer,
      fullRouteAnalyzer,
      fullNetworkAnalyzer
    )
  }

  private def createPostProcessor(): PostProcessor = {
    val orphanNodeUpdater = new OrphanNodeUpdater(database)
    val orphanRouteUpdater = new OrphanRouteUpdater(database)
    val statisticsUpdater = new StatisticsUpdater(database)

    new PostProcessor(
      orphanNodeUpdater,
      orphanRouteUpdater,
      statisticsUpdater
    )
  }

  // Helper class to group repositories
  private case class RepositoryGroup(
    networkRepository: NetworkRepository,
    routeRepository: RouteRepository,
    nodeRepository: NodeRepository,
    changeSetRepository: ChangeSetRepository,
    networkInfoRepository: NetworkInfoRepository,
    rawDataRepository: RawDataRepository,
    overpassRepository: OverpassRepository
  )
}
