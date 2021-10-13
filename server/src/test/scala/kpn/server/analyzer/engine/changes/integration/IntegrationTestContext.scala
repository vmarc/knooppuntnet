package kpn.server.analyzer.engine.changes.integration

import kpn.core.data.Data
import kpn.core.test.OverpassData
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoChangeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerNoop
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
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
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileChangeAnalyzerImpl
import kpn.server.analyzer.full.FullAnalyzer
import kpn.server.analyzer.full.FullAnalyzerImpl
import kpn.server.analyzer.full.network.FullNetworkAnalyzerImpl
import kpn.server.analyzer.full.node.FullNodeAnalyzerImpl
import kpn.server.analyzer.full.route.FullRouteAnalyzerImpl
import kpn.server.analyzer.load.AnalysisDataInitializer
import kpn.server.analyzer.load.AnalysisDataInitializerImpl
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
  countryAnalyzer: CountryAnalyzer
) extends MockFactory {

  val before: Data = dataBefore.data
  val after: Data = dataAfter.data

  val analysisContext = new AnalysisContext()

  private val overpassRepository = new OverpassRepositoryMock(before, after)
  private implicit val analysisExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  private val changeSetRepository = new ChangeSetRepositoryImpl(database)
  private val nodeRepository = new NodeRepositoryImpl(database)
  private val routeRepository = new RouteRepositoryImpl(database)
  private val networkRepository = new NetworkRepositoryImpl(database)
  private val changeSetInfoRepository = new ChangeSetInfoRepositoryImpl(database)
  private val networkInfoRepository = new NetworkInfoRepositoryImpl(database)

  private val taskRepository = stub[TaskRepository]
  private val blacklistRepository = stub[BlacklistRepository]
  (blacklistRepository.get _).when(*).returns(Blacklist())

  private val tileCalculator = new TileCalculatorImpl()
  private val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
  private val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
  private val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
  private val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
  private val masterRouteAnalyzer = new MasterRouteAnalyzerImpl(
    analysisContext,
    routeCountryAnalyzer,
    routeLocationAnalyzer,
    routeTileAnalyzer
  )

  private val elementIdAnalyzer = new ElementIdAnalyzerImpl

  val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzerImpl(nodeRepository)

  private val nodeAnalyzer: NodeAnalyzer = {
    val nodeCountryAnalyzer = new NodeCountryAnalyzerImpl(countryAnalyzer)
    new NodeAnalyzerImpl(
      nodeCountryAnalyzer,
      new NodeTileAnalyzerNoop,
      new NodeLocationsAnalyzerNoop,
      nodeRouteReferencesAnalyzer
    )
  }

  private val tileChangeAnalyzer = new TileChangeAnalyzerImpl(
    taskRepository,
    routeTileCalculator
  )

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
      masterRouteAnalyzer,
      tileChangeAnalyzer,
      routeRepository,
      analysisExecutionContext
    )
  }

  private val nodeChangeAnalyzer = new NodeChangeAnalyzerImpl(
    analysisContext,
    blacklistRepository
  )

  private val bulkNodeAnalyzer = new BulkNodeAnalyzerImpl(
    database,
    overpassRepository,
    nodeAnalyzer
  )

  private val nodeChangeProcessor = new NodeChangeProcessorImpl(
    analysisContext,
    bulkNodeAnalyzer,
    nodeChangeAnalyzer,
    nodeRepository
  )

  private val networkInfoMasterAnalyzer = {

    val networkInfoRouteAnalyzer = new NetworkInfoRouteAnalyzer(database)
    val networkInfoNodeAnalyzer = new NetworkInfoNodeAnalyzer(database)
    val networkInfoChangeAnalyzer = new NetworkInfoChangeAnalyzer(database)
    val networkCountryAnalyzer = new NetworkCountryAnalyzer(countryAnalyzer)
    val networkInfoExtraAnalyzer = new NetworkInfoExtraAnalyzer(overpassRepository)

    new NetworkInfoMasterAnalyzer(
      database,
      networkInfoRouteAnalyzer,
      networkInfoNodeAnalyzer,
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
    masterRouteAnalyzer,
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
    nodeRepository
  )
}
