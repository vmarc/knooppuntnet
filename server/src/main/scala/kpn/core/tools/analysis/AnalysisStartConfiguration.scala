package kpn.core.tools.analysis

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoChangeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
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
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl

class AnalysisStartConfiguration(options: AnalysisStartToolOptions) {

  val timestamp: Timestamp = Timestamp.analysisStart

  private val analysisContext = new AnalysisContext()
  private val database = Mongo.database(Mongo.client, options.databaseName)

  val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database)
  val routeRepository: RouteRepository = new RouteRepositoryImpl(database)
  val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)
  val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(database)

  private val locationAnalyzer = new LocationAnalyzerImpl(true)

  private val tileCalculator = new TileCalculatorImpl()

  private val nodeAnalyzer: NodeAnalyzer = {
    val nodeCountryAnalyzer = new NodeCountryAnalyzerImpl(locationAnalyzer)
    val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
    val nodeTileAnalyzer = new NodeTileAnalyzerImpl(nodeTileCalculator)
    val nodeLocationsAnalyzer = new NodeLocationsAnalyzerImpl(locationAnalyzer)
    val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzerImpl(nodeRepository)
    new NodeAnalyzerImpl(
      nodeCountryAnalyzer: NodeCountryAnalyzer,
      nodeTileAnalyzer: NodeTileAnalyzer,
      nodeLocationsAnalyzer: NodeLocationsAnalyzer,
      nodeRouteReferencesAnalyzer: NodeRouteReferencesAnalyzer
    )
  }

  private val routeTileAnalyzer = {
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    new RouteTileAnalyzer(routeTileCalculator)
  }

  val overpassRepository: OverpassRepository = {
    val overpassQueryExecutor = new OverpassQueryExecutorImpl()
    new OverpassRepositoryImpl(overpassQueryExecutor)
  }

  val changeSetRepository: ChangeSetRepository = new ChangeSetRepositoryImpl(database)

  val masterRouteAnalyzer: MasterRouteAnalyzer = {
    val routeLocator = new RouteLocatorImpl(locationAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerImpl(routeRepository, routeLocator)
    val routeCountryAnalyzer = new RouteCountryAnalyzer(locationAnalyzer)
    new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
  }

  val bulkNodeAnalyzer: BulkNodeAnalyzer = new BulkNodeAnalyzerImpl(
    database,
    overpassRepository,
    nodeAnalyzer
  )

  val networkInfoRepository: NetworkInfoRepository = new NetworkInfoRepositoryImpl(database)

  val networkInfoMasterAnalyzer: NetworkInfoMasterAnalyzer = {

    val networkInfoRouteAnalyzer = new NetworkInfoRouteAnalyzer(database)
    val networkInfoNodeDocAnalyzer = new NetworkInfoNodeDocAnalyzer(database)
    val networkInfoChangeAnalyzer = new NetworkInfoChangeAnalyzer(database)
    val networkCountryAnalyzer = new NetworkCountryAnalyzer(locationAnalyzer)
    val networkInfoExtraAnalyzer = new NetworkInfoExtraAnalyzer(overpassRepository)

    new NetworkInfoMasterAnalyzer(
      database,
      networkInfoRouteAnalyzer,
      networkInfoNodeDocAnalyzer,
      networkInfoChangeAnalyzer,
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
}
