package kpn.core.tools.analysis

import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.RouteLocator
import kpn.server.analyzer.engine.analysis.network.base.BaseNetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkRouteAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.BaseNodeMainAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeCountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeTileAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.NodeMainAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeRouteReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteIdsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteSuperSegmentAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.analyzer.full.MainFullAnalyzer
import kpn.server.analyzer.full.analyzers.FullAnalysisPipeline
import kpn.server.analyzer.full.analyzers.FullBaseNetworkAnalyzer
import kpn.server.analyzer.full.analyzers.FullBaseNodeAnalyzer
import kpn.server.analyzer.full.analyzers.FullBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.FullNetworkAnalyzer
import kpn.server.analyzer.full.analyzers.FullNodeAnalyzer
import kpn.server.analyzer.full.analyzers.FullRouteAnalyzer
import kpn.server.analyzer.full.analyzers.InitialNetworkChangeBuilder
import kpn.server.analyzer.full.analyzers.InitialNodeChangeBuilder
import kpn.server.analyzer.full.analyzers.InitialRouteChangeBuilder
import kpn.server.analyzer.full.analyzers.SingleBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.SingleRouteAnalyzer
import kpn.server.overpass.OverpassRepository
import kpn.server.overpass.OverpassRepositoryImpl
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RawDataRepositoryDevelopmentImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteTileRepository
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

class AnalysisConfiguration(
  databaseName: String,
  overpassUrl: String
) {

  // Database components
  private val mongoClient = Mongo.client
  private val database = Mongo.database(mongoClient, databaseName)
  val analysisRepository = new AnalysisRepository(database)

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
    val networkRepository = new NetworkRepository(database)
    val routeRepository = new RouteRepository(database)
    val routeTileRepository = new RouteTileRepository(database)
    val nodeRepository = new NodeRepository(database)
    val changeSetRepository = new ChangeSetRepository(database)
    val networkInfoRepository = new NetworkInfoRepository(database)
    val rawDataRepository = new RawDataRepositoryDevelopmentImpl(database)
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl(overpassUrl)
    val overpassRepository = new OverpassRepositoryImpl(overpassQueryExecutor)

    RepositoryGroup(
      networkRepository,
      routeRepository,
      routeTileRepository,
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
    val nodeTileCalculator = new NodeTileCalculator(routeTileCache)
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
    val routeLocator = new RouteLocator(locationAnalyzer)
    val routeLocationAnalyzer = new BaseRouteLocationAnalyzerImpl(repositories.routeRepository, routeLocator)
    val routeCountryAnalyzer = new BaseRouteCountryAnalyzerImpl(locationAnalyzer, repositories.routeRepository)
    val lineSegmentTileCalculator = new LineSegmentTileCalculator(routeTileCache)
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
      repositories.routeTileRepository,
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
    val statisticsUpdater = new StatisticsUpdater(database)
    new PostProcessor(
      statisticsUpdater
    )
  }

  // Helper class to group repositories
  private case class RepositoryGroup(
    networkRepository: NetworkRepository,
    routeRepository: RouteRepository,
    routeTileRepository: RouteTileRepository,
    nodeRepository: NodeRepository,
    changeSetRepository: ChangeSetRepository,
    networkInfoRepository: NetworkInfoRepository,
    rawDataRepository: RawDataRepository,
    overpassRepository: OverpassRepository
  )
}
