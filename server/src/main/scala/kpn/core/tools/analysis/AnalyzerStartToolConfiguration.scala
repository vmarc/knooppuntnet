package kpn.core.tools.analysis

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContextImpl
import kpn.core.db.couch.Couch
import kpn.core.mongo.util.Mongo
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.OldMainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.NetworkInitialLoader
import kpn.server.analyzer.load.NetworkInitialLoaderImpl
import kpn.server.analyzer.load.NetworkInitialLoaderWorker
import kpn.server.analyzer.load.NetworkInitialLoaderWorkerImpl
import kpn.server.analyzer.load.NetworkLoaderImpl
import kpn.server.analyzer.load.NodeLoaderImpl
import kpn.server.analyzer.load.RouteLoader
import kpn.server.analyzer.load.RouteLoaderImpl
import kpn.server.analyzer.load.orphan.route.OrphanRoutesLoaderWorkerImpl
import kpn.server.json.Json
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.AnalysisRepositoryImpl
import kpn.server.repository.BlackListRepositoryImpl
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.server.repository.FactRepositoryImpl
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

import java.util.concurrent.Executor

class AnalyzerStartToolConfiguration(val analysisExecutor: Executor, options: AnalyzerStartToolOptions) {

  val dirs: Dirs = Dirs()

  private val mongoEnabled = false
  private val mongoDatabase = Mongo.database(Mongo.client, "kpn-test")

  private val couchConfig = Couch.config
  private val analysisDatabase = new DatabaseImpl(DatabaseContextImpl(couchConfig, Json.objectMapper, options.analysisDatabaseName))
  private val changeDatabase = new DatabaseImpl(DatabaseContextImpl(couchConfig, Json.objectMapper, options.changeDatabaseName))

  val analysisContext = new AnalysisContext()

  val relationAnalyzer: RelationAnalyzer = new RelationAnalyzerImpl(analysisContext)

  val oldNodeAnalyzer: OldNodeAnalyzer = new OldNodeAnalyzerImpl()
  val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, oldNodeAnalyzer)


  private val locationConfiguration = new LocationConfigurationReader().read()
  private val routeLocator = new RouteLocatorImpl(locationConfiguration)
  val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)

  val networkRepository = new NetworkRepositoryImpl(mongoDatabase, analysisDatabase, mongoEnabled)
  val routeRepository = new RouteRepositoryImpl(mongoDatabase, analysisDatabase, mongoEnabled)
  val nodeRepository = new NodeRepositoryImpl(mongoDatabase, analysisDatabase, mongoEnabled)

  private val tileCalculator = new TileCalculatorImpl()
  private val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)

  val nodeAnalyzer: NodeAnalyzer = {
    val nodeCountryAnalyzer = new NodeCountryAnalyzerImpl(countryAnalyzer)
    val nodeTileAnalyzer = new NodeTileAnalyzerImpl(nodeTileCalculator)
    val nodeLocationsAnalyzer = new NodeLocationsAnalyzerImpl(locationConfiguration, true)
    val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzerImpl(nodeRepository)
    new NodeAnalyzerImpl(
      nodeCountryAnalyzer: NodeCountryAnalyzer,
      nodeTileAnalyzer: NodeTileAnalyzer,
      nodeLocationsAnalyzer: NodeLocationsAnalyzer,
      nodeRouteReferencesAnalyzer: NodeRouteReferencesAnalyzer
    )
  }
  private val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
  private val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)

  val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(
    analysisDatabase,
    networkRepository,
    routeRepository,
    nodeRepository,
    relationAnalyzer,
    nodeAnalyzer
  )

  val statusRepository: StatusRepository = new StatusRepositoryImpl(dirs)

  val changeSetInfoApi = new ChangeSetInfoApiImpl(dirs.changeSets)

  val nonCachingExecutor = new OverpassQueryExecutorImpl()

  val cachingExecutor = new CachingOverpassQueryExecutor(dirs.cache, nonCachingExecutor)

  val osmChangeRepository = new OsmChangeRepository(dirs.replicate)

  val analysisData: AnalysisData = AnalysisData()

  val changeSetRepository = new ChangeSetRepositoryImpl(null, changeDatabase, false)

  private val blackListRepository = new BlackListRepositoryImpl(null, analysisDatabase, false)

  val orphanRepository = new OrphanRepositoryImpl(mongoDatabase)

  val factRepository = new FactRepositoryImpl(null, analysisDatabase, false)

  val nodeLoader = new NodeLoaderImpl(
    analysisContext,
    nonCachingExecutor,
    countryAnalyzer,
    oldNodeAnalyzer
  )

  val routeLoader: RouteLoader = new RouteLoaderImpl(
    cachingExecutor
  )

  val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)

  val routeLocationAnalyzer = new RouteLocationAnalyzerImpl(
    routeRepository,
    routeLocator
  )

  val routeAnalyzer = new MasterRouteAnalyzerImpl(
    analysisContext,
    routeCountryAnalyzer,
    routeLocationAnalyzer,
    routeTileAnalyzer,
    routeNodeInfoAnalyzer
  )

  val networkLoader = new NetworkLoaderImpl(cachingExecutor)
  val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)

  val oldNodeLocationAnalyzer = new OldNodeLocationAnalyzerImpl(locationConfiguration, true)
  val oldMainNodeAnalyzer = new OldMainNodeAnalyzerImpl(
    countryAnalyzer,
    oldNodeLocationAnalyzer
  )

  val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(analysisContext, oldMainNodeAnalyzer, oldNodeAnalyzer)

  val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
    analysisContext,
    countryAnalyzer,
    relationAnalyzer,
    routeAnalyzer
  )


  val networkAnalyzer = new NetworkAnalyzerImpl(relationAnalyzer, networkNodeAnalyzer, networkRouteAnalyzer)

  private val networkInitialLoaderWorker: NetworkInitialLoaderWorker = new NetworkInitialLoaderWorkerImpl(
    analysisContext,
    analysisRepository,
    networkLoader,
    networkRelationAnalyzer,
    networkAnalyzer,
    blackListRepository
  )

  val networkInitialLoader: NetworkInitialLoader = new NetworkInitialLoaderImpl(
    analysisExecutor,
    networkInitialLoaderWorker
  )

  val orphanRoutesLoaderWorker = new OrphanRoutesLoaderWorkerImpl(
    analysisContext,
    routeLoader,
    routeRepository,
    routeAnalyzer,
    relationAnalyzer,
    nodeRepository,
    networkNodeAnalyzer,
    nodeAnalyzer
  )

  val replicationId: ReplicationId = ReplicationId(1)
  private val beginOsmChange = osmChangeRepository.get(replicationId)
  val timestamp: Timestamp = TimestampUtil.relativeSeconds(beginOsmChange.timestampUntil.get, -1)

  val changeSetContext: ChangeSetContext = ChangeSetContext(
    replicationId,
    ChangeSet(
      0,
      timestamp,
      timestamp,
      timestamp,
      timestamp,
      timestamp,
      Seq.empty
    )
  )
}
