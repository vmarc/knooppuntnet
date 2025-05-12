package kpn.server.analyzer.engine.changes.integration

import kpn.core.data.Data
import kpn.core.test.OverpassData
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
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessorPipeline
import kpn.server.analyzer.engine.changes.ChangeSaver
import kpn.server.analyzer.engine.changes.ElementIdAnalyzerImpl
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.network.BaseNetworkChangeProcessor
import kpn.server.analyzer.engine.changes.network.NetworkChangeAnalyzer
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeProcessor
import kpn.server.analyzer.engine.changes.node.base.NodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.node.main.NodeChangeProcessor
import kpn.server.analyzer.engine.changes.route.BaseRouteChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.BaseRouteChangeCreateProcessor
import kpn.server.analyzer.engine.changes.route.BaseRouteChangeDeleteProcessor
import kpn.server.analyzer.engine.changes.route.BaseRouteChangeDeleter
import kpn.server.analyzer.engine.changes.route.BaseRouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.BaseRouteChangeUpdateProcessor
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessor
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileChangeAnalyzerImpl
import kpn.server.analyzer.engine.tile.RouteTileChangeAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
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
import kpn.server.analyzer.load.AnalysisDataInitializer
import kpn.server.analyzer.load.AnalysisDataInitializerImpl
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

  private val overpassRepository = new OverpassRepositoryMock(before, after)
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

  private val tileCalculator = new TileCalculatorImpl()
  private val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(tileCalculator)
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
    val routeStructureRowsAnalyzer = new RouteStructureRowsAnalyzer(baseRouteRepository)
    val routeParentAnalyzer = new RouteParentAnalyzer(baseRouteRepository)
    val networkReferencesAnalyzer = new RouteNetworkReferencesAnalyzer(networkRepository)
    new RouteMainAnalyzer(
      routeBoundsAnalyzer,
      routeStructureRowsAnalyzer,
      routeParentAnalyzer,
      networkReferencesAnalyzer,
    )
  }

  private val elementIdAnalyzer = new ElementIdAnalyzerImpl

  val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzer(nodeRepository)

  private val routeTileChangeAnalyzer = new RouteTileChangeAnalyzerImpl()

  private val changeSetInfoUpdater = new ChangeSetInfoUpdaterImpl(
    changeSetInfoRepository,
    taskRepository
  )

  private val routeChangeProcessor: RouteChangeProcessor = {

    val routeChangeAnalyzer = new BaseRouteChangeAnalyzer(
      analysisContext,
      blacklistRepository,
      elementIdAnalyzer
    )

    new RouteChangeProcessor(
      analysisContext,
      routeChangeAnalyzer,
      overpassRepository,
      routeMainAnalyzer,
      routeRepository,
      analysisExecutionContext
    )
  }

  private val nodeChangeAnalyzer = new NodeChangeAnalyzer(
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

  private val nodeChangeProcessor = new NodeChangeProcessor(
    bulkNodeAnalyzer,
    nodeChangeAnalyzer,
    nodeRepository,
    networkRepository,
    nodeTileChangeAnalyzer
  )

  private val baseNetworkMainAnalyzer = new BaseNetworkMainAnalyzer()

  private val networkMainAnalyzer = {

    val networkInfoRouteAnalyzer = new NetworkRouteAnalyzer(database)
    val networkInfoNodeDocAnalyzer = new NetworkNodeDocAnalyzer(database)
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
    val nodeTileCalculator: NodeTileCalculator = new NodeTileCalculatorImpl(new TileCalculatorImpl())
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

  private val orphanNodeUpdater = new OrphanNodeUpdater(database)
  private val orphanRouteUpdater = new OrphanRouteUpdater(database)
  private val statisticsUpdater = new StatisticsUpdater(database)

  private val baseRouteDocBuilder = new BaseRouteDocBuilder()

  val changeProcessorPipeline: ChangeProcessorPipeline = {

    val changeSaver = new ChangeSaver(
      changeSetRepository,
      networkInfoRepository
    )

    val baseNodeBulkAnalyzer = new BaseNodeBulkAnalyzerImpl(
      rawDataRepository,
      baseNodeMainAnalyzer,
    )

    val networkChangeProcessor = {

      new NetworkChangeProcessor(
        analysisContext,
        networkRepository,
        networkMainAnalyzer,
      )
    }

    val baseNetworkChangeProcessor = {
      val networkChangeAnalyzer = new NetworkChangeAnalyzer(
        analysisContext,
        blacklistRepository
      )
      new BaseNetworkChangeProcessor(
        analysisContext,
        networkChangeAnalyzer,
        rawDataRepository,
        networkRepository,
        baseNetworkMainAnalyzer,
      )
    }

    val baseNodeChangeProcessor = new BaseNodeChangeProcessor(
      analysisContext,
      nodeChangeAnalyzer: NodeChangeAnalyzer,
      nodeRepository: NodeRepository,
      baseNodeBulkAnalyzer: BaseNodeBulkAnalyzer
    )

    val baseRouteChangeProcessor = {
      val routeChangeAnalyzer = new BaseRouteChangeAnalyzer(
        analysisContext,
        blacklistRepository,
        elementIdAnalyzer
      )

      val baseRouteChangeCreateProcessor = new BaseRouteChangeCreateProcessor(
        analysisContext,
        rawDataRepository,
        routeRepository,
        baseRouteMainAnalyzer,
        baseRouteDocBuilder
      )

      val baseRouteDeleter = new BaseRouteChangeDeleter(
        analysisContext,
        routeRepository,
      )

      val baseRouteChangeUpdateProcessor = new BaseRouteChangeUpdateProcessor(
        analysisContext,
        rawDataRepository,
        routeRepository,
        baseRouteMainAnalyzer,
        baseRouteDocBuilder,
        routeTileChangeAnalyzer,
        baseRouteDeleter
      )

      val baseRouteChangeDeleteProcessor = new BaseRouteChangeDeleteProcessor(
        baseRouteDeleter
      )

      new BaseRouteChangeProcessor(
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
    orphanNodeUpdater,
    orphanRouteUpdater,
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
    val fullBaseRouteAnalyzer = new FullBaseRouteAnalyzer(
      rawDataRepository,
      routeRepository,
      baseRouteMainAnalyzer,
      baseRouteDocBuilder
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

  val analysisDataInitializer: AnalysisDataInitializer = new AnalysisDataInitializerImpl(
    analysisContext,
    networkRepository,
    routeRepository,
    nodeRepository
  )
}
