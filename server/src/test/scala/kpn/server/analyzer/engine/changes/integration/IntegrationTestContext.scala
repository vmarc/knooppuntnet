package kpn.server.analyzer.engine.changes.integration

import kpn.core.data.Data
import kpn.core.test.OverpassData
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoChangeAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkInfoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeRouteReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSaverImpl
import kpn.server.analyzer.engine.changes.ElementIdAnalyzerImpl
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.network.NetworkChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessorImpl
import kpn.server.analyzer.engine.changes.network.info.NetworkInfoChangeProcessorImpl
import kpn.server.analyzer.engine.changes.network.info.NetworkInfoImpactAnalyzer
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.node.NodeChangeProcessorImpl
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessorImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileChangeAnalyzerImpl
import kpn.server.analyzer.engine.tile.RouteTileChangeAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.full.FullAnalyzer
import kpn.server.analyzer.full.FullAnalyzerImpl
import kpn.server.analyzer.full.network.FullNetworkAnalyzerImpl
import kpn.server.analyzer.full.node.FullNodeAnalyzerImpl
import kpn.server.analyzer.full.route.FullRouteAnalyzerImpl
import kpn.server.analyzer.load.AnalysisDataInitializer
import kpn.server.analyzer.load.AnalysisDataInitializerImpl
import kpn.server.repository.BaseRouteRepositoryImpl
import kpn.server.repository.BlacklistRepository
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.server.repository.NetworkInfoRepositoryImpl
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
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
  private val baseRouteRepository = new BaseRouteRepositoryImpl(database)
  private val networkRepository = new NetworkRepositoryImpl(database)
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
    new RouteMainAnalyzer(
      routeBoundsAnalyzer,
      routeStructureRowsAnalyzer,
      routeParentAnalyzer
    )
  }

  private val elementIdAnalyzer = new ElementIdAnalyzerImpl

  val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzer(nodeRepository)

  private val routeTileChangeAnalyzer = new RouteTileChangeAnalyzerImpl()

  private val networkChangeProcessor = {

    val networkChangeAnalyzer = new NetworkChangeAnalyzerImpl(
      analysisContext,
      blacklistRepository
    )

    new NetworkChangeProcessorImpl(
      database,
      analysisContext,
      networkChangeAnalyzer,
      overpassRepository
    )
  }

  private val changeSetInfoUpdater = new ChangeSetInfoUpdaterImpl(
    changeSetInfoRepository,
    taskRepository
  )

  private val routeChangeProcessor: RouteChangeProcessor = {

    val routeChangeAnalyzer = new RouteChangeAnalyzer(
      analysisContext,
      blacklistRepository,
      elementIdAnalyzer
    )

    new RouteChangeProcessorImpl(
      analysisContext,
      routeChangeAnalyzer,
      overpassRepository,
      baseRouteMainAnalyzer,
      routeMainAnalyzer,
      routeTileChangeAnalyzer,
      routeRepository,
      baseRouteRepository,
      analysisExecutionContext
    )
  }

  private val nodeChangeAnalyzer = new NodeChangeAnalyzerImpl(
    analysisContext,
    blacklistRepository
  )

  private val bulkNodeAnalyzer = new BulkNodeAnalyzerImpl()

  private val nodeTileChangeAnalyzer = new NodeTileChangeAnalyzerImpl(
    new TileDataNodeBuilderImpl()
  )

  private val nodeChangeProcessor = new NodeChangeProcessorImpl(
    analysisContext,
    bulkNodeAnalyzer,
    nodeChangeAnalyzer,
    nodeRepository,
    nodeTileChangeAnalyzer
  )

  private val networkInfoMasterAnalyzer = {

    val networkInfoRouteAnalyzer = new NetworkInfoRouteAnalyzer(database)
    val networkInfoNodeDocAnalyzer = new NetworkInfoNodeDocAnalyzer(database)
    val networkInfoChangeAnalyzer = new NetworkInfoChangeAnalyzer(database)
    val networkCountryAnalyzer = new NetworkCountryAnalyzer(locationAnalyzer)
    val networkInfoExtraAnalyzer = new NetworkInfoExtraAnalyzer(overpassRepository)

    new NetworkMainAnalyzer(
      database,
      networkInfoRouteAnalyzer,
      networkInfoNodeDocAnalyzer,
      networkInfoChangeAnalyzer,
      networkCountryAnalyzer,
      networkInfoExtraAnalyzer
    )
  }

  private val networkInfoChangeProcessor = {

    val networkInfoImpactAnalyzer = new NetworkInfoImpactAnalyzer(database)

    new NetworkInfoChangeProcessorImpl(
      database,
      networkInfoImpactAnalyzer,
      networkInfoMasterAnalyzer
    )
  }

  private val fullNetworkAnalyzer = new FullNetworkAnalyzerImpl(
    overpassRepository,
    networkRepository
  )

  private val fullRouteAnalyzer = new FullRouteAnalyzerImpl(
    overpassRepository,
    routeRepository,
    baseRouteRepository,
    baseRouteMainAnalyzer,
    routeMainAnalyzer,
    analysisExecutionContext: ExecutionContext
  )

  private val fullNodeAnalyzer = new FullNodeAnalyzerImpl(
    database,
    overpassRepository,
    nodeRepository,
    bulkNodeAnalyzer,
    analysisExecutionContext
  )

  private val orphanNodeUpdater = new OrphanNodeUpdater(database)
  private val orphanRouteUpdater = new OrphanRouteUpdater(database)
  private val statisticsUpdater = new StatisticsUpdater(database)

  val changeProcessor: ChangeProcessor = {

    val changeSaver = new ChangeSaverImpl(
      changeSetRepository,
      networkInfoRepository
    )

    new ChangeProcessor(
      networkChangeProcessor,
      routeChangeProcessor,
      nodeChangeProcessor,
      networkInfoChangeProcessor,
      changeSetInfoUpdater,
      changeSaver
    )
  }

  val postProcessor: PostProcessor = new PostProcessor(
    networkInfoMasterAnalyzer,
    orphanNodeUpdater,
    orphanRouteUpdater,
    statisticsUpdater
  )

  val fullAnalyzer: FullAnalyzer = new FullAnalyzerImpl(
    fullNetworkAnalyzer,
    fullRouteAnalyzer,
    fullNodeAnalyzer,
    postProcessor
  )

  val analysisDataInitializer: AnalysisDataInitializer = new AnalysisDataInitializerImpl(
    analysisContext,
    networkRepository,
    routeRepository,
    baseRouteRepository,
    nodeRepository
  )
}
