package kpn.server.analyzer.engine.changes.integration

import kpn.core.data.Data
import kpn.core.test.OverpassData
import kpn.core.test.Timestamps
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerFixed
import kpn.server.analyzer.engine.analysis.network.base.BaseNetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkRouteAnalyzer
import kpn.server.analyzer.engine.analysis.node.BaseNodeBulkAnalyzer
import kpn.server.analyzer.engine.analysis.node.BaseNodeBulkAnalyzerImpl
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
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteIdsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteSuperSegmentAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessorPipeline
import kpn.server.analyzer.engine.changes.ChangeSaverImpl
import kpn.server.analyzer.engine.changes.ElementIdAnalyzerImpl
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.network.base.BaseNetworkChangeAnalyzer
import kpn.server.analyzer.engine.changes.network.base.BaseNetworkChangeProcessorImpl
import kpn.server.analyzer.engine.changes.network.main.NetworkChangeProcessorImpl
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeProcessorImpl
import kpn.server.analyzer.engine.changes.node.main.NodeChangeProcessorImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeCreateProcessorImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeDeleteProcessorImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeDeleterImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeProcessorImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeUpdateProcessorImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeUpdateTileProcessorImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeUpdateWayProcessorImpl
import kpn.server.analyzer.engine.changes.route.main.RouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.main.RouteChangeProcessorImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileChangeAnalyzerImpl
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
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
import kpn.server.analyzer.load.AnalysisContextLoader
import kpn.server.analyzer.load.AnalysisContextLoaderImpl
import kpn.server.repository.BlacklistRepository
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.server.repository.NetworkInfoRepositoryImpl
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RawDataRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class IntegrationTestContext(
  val database: Database,
  dataBefore: OverpassData,
  dataAfter: OverpassData,
  locationAnalyzer: LocationAnalyzer
) extends MockFactory {

  val before: Data = dataBefore.data
  val after: Data = dataAfter.data

  val analysisContext = new AnalysisContext()

  val overpassRepository = new OverpassRepositoryMock
  overpassRepository.setData(Timestamps.before, before)
  overpassRepository.setData(Timestamps.after, after)

  private implicit val analysisExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  private val changeSetRepository = new ChangeSetRepositoryImpl(database)
  val nodeRepository = new NodeRepositoryImpl(database)

  private val routeRepository = new RouteRepositoryImpl(database)
  private val baseRouteRepository = new RouteRepositoryImpl(database)
  private val networkRepository = new NetworkRepositoryImpl(database)

  private val rawDataRepository = new RawDataRepositoryImpl(overpassRepository)
  private val changeSetInfoRepository = new ChangeSetInfoRepositoryImpl(database)
  private val networkInfoRepository = new NetworkInfoRepositoryImpl(database)

  private val taskRepository = stub[TaskRepository]
  private val blacklistRepository = stub[BlacklistRepository]
  (blacklistRepository.get _).when(*).returns(Blacklist())

  private val routeTileCache = new RouteTileCache()
  private val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(routeTileCache)
  private val routeTileAnalyzer = new BaseRouteTileAnalyzer(lineSegmentTileCalculator)
  private val routeCountryAnalyzer = new BaseRouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
  private val routeLocationAnalyzer = new BaseRouteLocationAnalyzerMock()
  private val baseRouteMainAnalyzer = new BaseRouteMainAnalyzer(
    routeCountryAnalyzer,
    routeLocationAnalyzer,
    routeTileAnalyzer
  )
  private val routeMainAnalyzer = {
    val routeBoundsAnalyzer = new RouteBoundsAnalyzer(baseRouteRepository)
    val routeIdsAnalyzer = new RouteIdsAnalyzer(baseRouteRepository)
    val routeSuperSegmentAnalyzer = new RouteSuperSegmentAnalyzer(routeRepository)
    val routeStructureRowsAnalyzer = new RouteStructureRowsAnalyzer(baseRouteRepository)
    val routeParentAnalyzer = new RouteParentAnalyzer(baseRouteRepository)
    val networkReferencesAnalyzer = new RouteNetworkReferencesAnalyzer(networkRepository)
    new RouteMainAnalyzer(
      routeBoundsAnalyzer,
      routeIdsAnalyzer,
      routeSuperSegmentAnalyzer,
      routeStructureRowsAnalyzer,
      routeParentAnalyzer,
      networkReferencesAnalyzer,
    )
  }

  private val elementIdAnalyzer = new ElementIdAnalyzerImpl(analysisContext)

  val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzer(nodeRepository)

  private val routeTileChangeAnalyzer = new BaseRouteChangeUpdateTileProcessorImpl(routeRepository)

  private val changeSetInfoUpdater = new ChangeSetInfoUpdaterImpl(
    changeSetInfoRepository,
    taskRepository
  )

  private val routeChangeProcessor: RouteChangeProcessor = new RouteChangeProcessorImpl(
    analysisContext,
    overpassRepository,
    routeMainAnalyzer,
    routeRepository,
    analysisExecutionContext
  )

  private val nodeChangeAnalyzer = new BaseNodeChangeAnalyzer(
    analysisContext,
    blacklistRepository
  )

  private val nodeTileChangeAnalyzer = new NodeTileChangeAnalyzerImpl(
    new TileDataNodeBuilderImpl()
  )

  private val bulkNodeAnalyzer = {
    val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzer(nodeRepository)
    val nodeNetworkReferencesAnalyzer = new NodeNetworkReferencesAnalyzer(networkRepository)
    val nodeMainAnalyzer = new NodeMainAnalyzer(
      nodeRouteReferencesAnalyzer,
      nodeNetworkReferencesAnalyzer,
    )
    new BulkNodeAnalyzer(
      rawDataRepository,
      nodeMainAnalyzer,
      nodeRepository,
    )
  }

  private val nodeChangeProcessor = new NodeChangeProcessorImpl(
    bulkNodeAnalyzer,
    nodeChangeAnalyzer,
    nodeRepository,
    networkRepository,
    nodeTileChangeAnalyzer
  )

  private val baseNetworkMainAnalyzer = new BaseNetworkMainAnalyzer()

  private val networkMainAnalyzer = {

    val networkInfoRouteAnalyzer = new NetworkRouteAnalyzer(routeRepository)
    val networkInfoNodeDocAnalyzer = new NetworkNodeDocAnalyzer(nodeRepository)
    val networkCountryAnalyzer = new NetworkCountryAnalyzer(locationAnalyzer)
    val networkInfoExtraAnalyzer = new NetworkExtraAnalyzer(overpassRepository)

    new NetworkMainAnalyzer(
      database,
      networkInfoRouteAnalyzer,
      networkInfoNodeDocAnalyzer,
      networkCountryAnalyzer,
      networkInfoExtraAnalyzer
    )
  }

  private val fullNetworkAnalyzer = {
    val initialNetworkChangeBuilder = new InitialNetworkChangeBuilder(
      changeSetRepository,
      networkInfoRepository
    )
    new FullNetworkAnalyzer(
      networkRepository,
      networkMainAnalyzer,
      initialNetworkChangeBuilder
    )
  }

  private val fullRouteAnalyzer = {
    val initialRouteChangeBuilder = new InitialRouteChangeBuilder(changeSetRepository)
    new FullRouteAnalyzer(
      routeRepository,
      routeMainAnalyzer,
      initialRouteChangeBuilder
    )
  }

  private val baseNodeMainAnalyzer = {
    val locationAnalyzer: LocationAnalyzer = new LocationAnalyzerFixed()
    val nodeTileCalculator: NodeTileCalculator = new NodeTileCalculatorImpl(new RouteTileCache())
    val baseNodeCountryAnalyzer = new BaseNodeCountryAnalyzer(locationAnalyzer)
    val baseNodeLocationAnalyzer = new BaseNodeLocationAnalyzer(locationAnalyzer)
    val baseNodeTileAnalyzer = new BaseNodeTileAnalyzer(nodeTileCalculator)
    new BaseNodeMainAnalyzer(
      baseNodeCountryAnalyzer,
      baseNodeLocationAnalyzer,
      baseNodeTileAnalyzer
    )
  }

  private val fullNodeAnalyzer = {
    val initialNodeChangeBuilder = new InitialNodeChangeBuilder(changeSetRepository)
    new FullNodeAnalyzer(
      rawDataRepository,
      nodeRepository,
      bulkNodeAnalyzer,
      initialNodeChangeBuilder
    )
  }

  private val statisticsUpdater = new StatisticsUpdater(database)

  private val baseRouteDocBuilder = new BaseRouteDocBuilder()

  val changeProcessorPipeline: ChangeProcessorPipeline = {

    val changeSaver = new ChangeSaverImpl(
      changeSetRepository,
      networkInfoRepository
    )

    val baseNodeBulkAnalyzer = new BaseNodeBulkAnalyzerImpl(
      rawDataRepository,
      baseNodeMainAnalyzer,
    )

    val networkChangeProcessor = {

      new NetworkChangeProcessorImpl(
        analysisContext,
        networkRepository,
        networkMainAnalyzer,
      )
    }

    val baseNetworkChangeProcessor = {
      val baseNetworkChangeAnalyzer = new BaseNetworkChangeAnalyzer(
        analysisContext,
        blacklistRepository
      )
      new BaseNetworkChangeProcessorImpl(
        analysisContext,
        baseNetworkChangeAnalyzer,
        rawDataRepository,
        networkRepository,
        baseNetworkMainAnalyzer,
      )
    }

    val baseNodeChangeProcessor = new BaseNodeChangeProcessorImpl(
      analysisContext,
      nodeChangeAnalyzer: BaseNodeChangeAnalyzer,
      baseNodeBulkAnalyzer: BaseNodeBulkAnalyzer,
      nodeRepository: NodeRepository
    )

    val baseRouteChangeProcessor = {
      val routeChangeAnalyzer = new BaseRouteChangeAnalyzer(
        analysisContext,
        blacklistRepository,
        elementIdAnalyzer
      )

      val baseRouteChangeCreateProcessor = new BaseRouteChangeCreateProcessorImpl(
        analysisContext,
        rawDataRepository,
        routeRepository,
        baseRouteMainAnalyzer,
        baseRouteDocBuilder
      )

      val baseRouteDeleter = new BaseRouteChangeDeleterImpl(
        analysisContext,
        routeRepository,
      )

      val baseRouteChangeUpdateWayProcessor = new BaseRouteChangeUpdateWayProcessorImpl()

      val baseRouteChangeUpdateProcessor = new BaseRouteChangeUpdateProcessorImpl(
        analysisContext,
        rawDataRepository,
        routeRepository,
        baseRouteMainAnalyzer,
        baseRouteDocBuilder,
        baseRouteChangeUpdateWayProcessor,
        routeTileChangeAnalyzer,
        baseRouteDeleter
      )

      val baseRouteChangeDeleteProcessor = new BaseRouteChangeDeleteProcessorImpl(
        baseRouteDeleter
      )

      new BaseRouteChangeProcessorImpl(
        routeChangeAnalyzer,
        baseRouteChangeCreateProcessor,
        baseRouteChangeUpdateProcessor,
        baseRouteChangeDeleteProcessor,
      )
    }

    new ChangeProcessorPipeline(
      baseNodeChangeProcessor,
      baseNetworkChangeProcessor,
      baseRouteChangeProcessor,
      networkChangeProcessor,
      routeChangeProcessor,
      nodeChangeProcessor,
      changeSetInfoUpdater,
      changeSaver
    )
  }

  val postProcessor: PostProcessor = new PostProcessor(
    statisticsUpdater
  )

  val mainFullAnalyzer: MainFullAnalyzer = {
    val fullBaseNodeAnalyzer = new FullBaseNodeAnalyzer(
      rawDataRepository,
      nodeRepository,
      baseNodeMainAnalyzer,
    )
    val fullBaseNetworkAnalyzer = new FullBaseNetworkAnalyzer(
      rawDataRepository,
      baseNetworkMainAnalyzer,
      networkRepository
    )

    val singleBaseRouteAnalyzer = new SingleBaseRouteAnalyzer(
      rawDataRepository,
      routeRepository,
      changeSetRepository,
      baseRouteMainAnalyzer,
      baseRouteDocBuilder
    )

    val fullBaseRouteAnalyzer = new FullBaseRouteAnalyzer(
      rawDataRepository,
      routeRepository,
      baseRouteDocBuilder,
      singleBaseRouteAnalyzer
    )

    val fullAnalysisPipeline = new FullAnalysisPipeline(
      fullBaseNodeAnalyzer,
      fullBaseRouteAnalyzer,
      fullBaseNetworkAnalyzer,
      fullNodeAnalyzer,
      fullRouteAnalyzer,
      fullNetworkAnalyzer
    )

    new MainFullAnalyzer(
      fullAnalysisPipeline,
      postProcessor
    )
  }

  val analysisDataInitializer: AnalysisContextLoader = new AnalysisContextLoaderImpl(
    analysisContext,
    networkRepository,
    routeRepository,
    nodeRepository
  )
}
