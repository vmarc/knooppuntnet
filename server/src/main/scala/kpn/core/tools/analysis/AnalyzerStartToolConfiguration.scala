package kpn.core.tools.analysis

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContextImpl
import kpn.core.db.couch.Couch
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.server.analyzer.engine.DatabaseIndexer
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.RouteLocatorImpl
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.MainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.Elements
import kpn.server.analyzer.engine.tile.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
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
import kpn.server.repository.NodeInfoBuilderImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import org.apache.commons.io.FileUtils

import java.io.File
import java.util.concurrent.Executor

class AnalyzerStartToolConfiguration(val analysisExecutor: Executor, options: AnalyzerStartToolOptions) {

  val dirs: Dirs = Dirs()

  private val couchConfig = Couch.config
  private val analysisDatabase = new DatabaseImpl(DatabaseContextImpl(couchConfig, Json.objectMapper, options.analysisDatabaseName))
  private val changeDatabase = new DatabaseImpl(DatabaseContextImpl(couchConfig, Json.objectMapper, options.changeDatabaseName))

  private val snapshotKnownElements = {
    val string = FileUtils.readFileToString(new File(AnalysisContext.networkTypeTaggingStartSnapshotFilename), "UTF-8")
    Json.value(string, classOf[Elements])
  }

  val analysisContext = new AnalysisContext(snapshotKnownElements, beforeNetworkTypeTaggingStart = true)

  val relationAnalyzer: RelationAnalyzer = new RelationAnalyzerImpl(analysisContext)

  val nodeAnalyzer: NodeAnalyzer = new NodeAnalyzerImpl()
  val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, nodeAnalyzer)


  private val locationConfiguration = new LocationConfigurationReader().read()
  private val routeLocator = new RouteLocatorImpl(locationConfiguration)
  val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
  val nodeLocationAnalyzer = new NodeLocationAnalyzerImpl(locationConfiguration, true)

  val networkRepository = new NetworkRepositoryImpl(analysisDatabase)
  val routeRepository = new RouteRepositoryImpl(analysisDatabase)
  val nodeRepository = new NodeRepositoryImpl(analysisDatabase)

  private val tileCalculator = new TileCalculatorImpl()
  private val nodeTileAnalyzer = new NodeTileAnalyzerImpl(tileCalculator)
  val nodeInfoBuilder = new NodeInfoBuilderImpl(nodeAnalyzer, nodeTileAnalyzer, nodeLocationAnalyzer)
  private val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)

  val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(
    analysisDatabase,
    networkRepository,
    routeRepository,
    nodeRepository,
    nodeInfoBuilder,
    relationAnalyzer
  )

  val statusRepository: StatusRepository = new StatusRepositoryImpl(dirs)

  val changeSetInfoApi = new ChangeSetInfoApiImpl(dirs.changeSets)

  val nonCachingExecutor = new OverpassQueryExecutorImpl()

  val cachingExecutor = new CachingOverpassQueryExecutor(dirs.cache, nonCachingExecutor)

  val osmChangeRepository = new OsmChangeRepository(dirs.replicate)

  val analysisDatabaseIndexer: DatabaseIndexer = new DatabaseIndexer(analysisDatabase, changeDatabase, null, null, null)

  val analysisData: AnalysisData = AnalysisData()

  val changeSetRepository = new ChangeSetRepositoryImpl(changeDatabase)

  private val blackListRepository = new BlackListRepositoryImpl(analysisDatabase)

  val orphanRepository = new OrphanRepositoryImpl(analysisDatabase)

  val factRepository = new FactRepositoryImpl(analysisDatabase)

  val nodeLoader = new NodeLoaderImpl(
    analysisContext,
    nonCachingExecutor,
    countryAnalyzer,
    nodeAnalyzer
  )

  val routeLoader: RouteLoader = new RouteLoaderImpl(
    cachingExecutor,
    countryAnalyzer
  )

  val routeLocationAnalyzer = new RouteLocationAnalyzerImpl(
    routeRepository,
    routeLocator
  )

  val routeAnalyzer = new MasterRouteAnalyzerImpl(
    analysisContext,
    routeLocationAnalyzer,
    routeTileAnalyzer,
    routeNodeInfoAnalyzer
  )

  val networkLoader = new NetworkLoaderImpl(cachingExecutor)
  val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)

  val mainNodeAnalyzer = new MainNodeAnalyzerImpl(
    countryAnalyzer,
    nodeLocationAnalyzer
  )

  val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(analysisContext, mainNodeAnalyzer, nodeAnalyzer)

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
    analysisRepository,
    nodeInfoBuilder,
    networkNodeAnalyzer
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
      Seq()
    )
  )

}
