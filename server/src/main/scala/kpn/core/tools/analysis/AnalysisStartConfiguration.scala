package kpn.core.tools.analysis

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.next.database.NextRepository
import kpn.core.tools.next.database.NextRepositoryImpl
import kpn.database.base.OldDatabase
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
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
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileFileBuilderImpl
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.TilesBuilder
import kpn.server.overpass.OverpassRepository
import kpn.server.overpass.OverpassRepositoryImpl
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.AnalysisRepositoryImpl
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.server.repository.NetworkInfoRepository
import kpn.server.repository.NetworkInfoRepositoryImpl
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RawDataRepositoryDevelopmentImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
import scala.concurrent.ExecutionContext

class AnalysisStartConfiguration(options: AnalysisStartToolOptions) {

  val timestamp: Timestamp = Timestamp.analysisStart

  private val database = Mongo.database(Mongo.client, options.databaseName)
  private val nextDatabase = Mongo.nextDatabase(Mongo.client, options.databaseName)
  val oldDatabase: OldDatabase = Mongo.oldDatabase(Mongo.client, options.databaseName)

  val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database)
  val routeRepository: RouteRepository = new RouteRepositoryImpl(database)
  val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)
  val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(database)
  val nextRepository: NextRepository = new NextRepositoryImpl(nextDatabase)

  private val locationAnalyzer = new LocationAnalyzerImpl(true, false)

  private val tileCalculator = new TileCalculatorImpl()

  private val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)

  private val routeTileAnalyzer = {
    val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(tileCalculator)
    new BaseRouteTileAnalyzer(lineSegmentTileCalculator)
  }

  val overpassQueryExecutor: OverpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  val overpassRepository: OverpassRepository = new OverpassRepositoryImpl(overpassQueryExecutor)

  val changeSetRepository: ChangeSetRepository = new ChangeSetRepositoryImpl(database)

  val baseNodeMainAnalyzer: BaseNodeMainAnalyzer = {
    val baseNodeLocationAnalyzer = new BaseNodeLocationAnalyzer(locationAnalyzer)
    val countryAnalyzer = new BaseNodeCountryAnalyzer(locationAnalyzer)
    val tileAnalyzer = new BaseNodeTileAnalyzer(nodeTileCalculator)
    new BaseNodeMainAnalyzer(
      countryAnalyzer,
      baseNodeLocationAnalyzer,
      tileAnalyzer
    )
  }

  val nodeMainAnalyzer: NodeMainAnalyzer = {
    val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzer(nodeRepository)
    val nodeNetworkReferencesAnalyzer = new NodeNetworkReferencesAnalyzer(networkRepository)
    new NodeMainAnalyzer(
      nodeRouteReferencesAnalyzer,
      nodeNetworkReferencesAnalyzer,
    )
  }

  val baseNetworkMainAnalyzer: BaseNetworkMainAnalyzer = new BaseNetworkMainAnalyzer

  val baseRouteMainAnalyzer: BaseRouteMainAnalyzer = {
    val routeLocator = new RouteLocatorImpl(locationAnalyzer)
    val routeLocationAnalyzer = new BaseRouteLocationAnalyzerImpl(routeRepository, routeLocator)
    val routeCountryAnalyzer = new BaseRouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
    new BaseRouteMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
  }

  val routeMainAnalyzer: RouteMainAnalyzer = {
    val routeBoundsAnalyzer = new RouteBoundsAnalyzer(routeRepository)
    val routeStructureRowsAnalyzer = new RouteStructureRowsAnalyzer(routeRepository)
    val routeParentAnalyzer = new RouteParentAnalyzer(routeRepository)
    val networkReferencesAnalyzer = new RouteNetworkReferencesAnalyzer(networkRepository)
    new RouteMainAnalyzer(
      routeBoundsAnalyzer,
      routeStructureRowsAnalyzer,
      routeParentAnalyzer,
      networkReferencesAnalyzer,
    )
  }

  val networkInfoRepository: NetworkInfoRepository = new NetworkInfoRepositoryImpl(database)

  val networkMainAnalyzer: NetworkMainAnalyzer = {

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

  val orphanNodeUpdater: OrphanNodeUpdater = new OrphanNodeUpdater(database)

  val orphanRouteUpdater: OrphanRouteUpdater = new OrphanRouteUpdater(database)

  val statisticsUpdater: StatisticsUpdater = new StatisticsUpdater(database)

  val changeSetContext: ChangeSetContext = ChangeSetContext(
    ReplicationId(1),
    ChangeSet(
      0,
      timestamp,
      timestamp,
      timestamp,
      timestamp,
      timestamp,
      Seq.empty
    ),
    ElementIds()
  )

  private val tileDir = s"${Dirs.root.getAbsolutePath}/tiles"
  private val tileDataNodeBuilder = new TileDataNodeBuilderImpl()

  private val executionContext: ExecutionContext = {
    val executor = buildExecutor()
    ExecutionContext.fromExecutor(executor)
  }

  val tilesBuilder: TilesBuilder = {
    val bitmapTileFileRepository = new TileFileRepositoryImpl(tileDir, "png")
    val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
    val tileFileBuilder = new TileFileBuilderImpl(bitmapTileFileRepository, vectorTileFileRepository)
    new TilesBuilder(
      bitmapTileFileRepository,
      vectorTileFileRepository,
      tileFileBuilder
    )(executionContext)
  }

  val rawDataRepository: RawDataRepository = new RawDataRepositoryDevelopmentImpl(database)

  val bulkNodeAnalyzer: BulkNodeAnalyzer = new BulkNodeAnalyzer(
    rawDataRepository,
    nodeMainAnalyzer,
    nodeRepository,
  )

  private def buildExecutor(): ThreadPoolTaskExecutor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(6)
    executor.setMaxPoolSize(6)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setThreadNamePrefix("analyzer-start-")
    executor.initialize()
    executor
  }
}
