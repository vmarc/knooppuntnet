package kpn.core.tools

import akka.actor.ActorSystem
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.server.analyzer.load.AnalysisDataLoader
import kpn.server.analyzer.load.NetworkLoaderImpl
import kpn.server.analyzer.load.NodeLoaderImpl
import kpn.server.analyzer.load.RouteLoader
import kpn.server.analyzer.load.RouteLoaderImpl
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.core.tools.config.AnalysisDataLoaderConfiguration
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.server.analyzer.engine.AnalysisContext
import kpn.server.analyzer.engine.CouchIndexer
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.analysis.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.server.repository.AnalysisDataRepository
import kpn.server.repository.AnalysisDataRepositoryImpl
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.AnalysisRepositoryImpl
import kpn.server.repository.BlackListRepositoryImpl
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.server.repository.FactRepositoryImpl
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class AnalyzerStartToolConfiguration(
  system: ActorSystem,
  analysisDatabase: Database,
  changeDatabase: Database
) {

  val dirs = Dirs()

  private val networkRepository = new NetworkRepositoryImpl(analysisDatabase)
  private val routeRepository = new RouteRepositoryImpl(analysisDatabase)
  private val nodeRepository = new NodeRepositoryImpl(analysisDatabase)

  val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(
    analysisDatabase,
    networkRepository,
    routeRepository,
    nodeRepository
  )

  val statusRepository: StatusRepository = new StatusRepositoryImpl(dirs)

  val changeSetInfoApi = new ChangeSetInfoApiImpl(dirs.changeSets, system)

  val nonCachingExecutor = new OverpassQueryExecutorWithThrotteling(system, new OverpassQueryExecutorImpl())

  val cachingExecutor = new CachingOverpassQueryExecutor(dirs.cache, nonCachingExecutor)

  val osmChangeRepository = new OsmChangeRepository(dirs.replicate)

  val analysisDataRepository: AnalysisDataRepository = new AnalysisDataRepositoryImpl(analysisDatabase)

  val analysisDatabaseIndexer: CouchIndexer = new CouchIndexer(
    analysisDatabase, AnalyzerDesign
  )

  val analysisData: AnalysisData = AnalysisData()

  val analysisContext = new AnalysisContext()

  val relationAnalyzer: RelationAnalyzer = new RelationAnalyzerImpl(analysisContext)

  val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)

  val changeSetRepository = new ChangeSetRepositoryImpl(
    changeDatabase
  )

  val changeSetInfoRepository = new ChangeSetInfoRepositoryImpl(
    changeDatabase
  )

  private val blackListRepository = new BlackListRepositoryImpl(
    analysisDatabase
  )

  val changeSetInfoUpdater = new ChangeSetInfoUpdater {
    override def changeSetInfo(changeSetId: Long): Unit = {}
  }

  val orphanRepository = new OrphanRepositoryImpl(
    analysisDatabase
  )

  val factRepository = new FactRepositoryImpl(
    analysisDatabase
  )

  val nodeLoader = new NodeLoaderImpl(
    nonCachingExecutor,
    cachingExecutor,
    countryAnalyzer
  )

  val routeLoader: RouteLoader = new RouteLoaderImpl(
    cachingExecutor,
    countryAnalyzer
  )

  val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl())

  val analysisDataLoader: AnalysisDataLoader = new AnalysisDataLoaderConfiguration(
    system,
    analysisContext,
    dirs.cache,
    nonCachingExecutor,
    cachingExecutor,
    orphanRepository,
    analysisRepository,
    factRepository,
    blackListRepository,
    changeSetInfoUpdater,
    relationAnalyzer,
    countryAnalyzer,
    nodeLoader,
    analysisDatabaseIndexer
  ).analysisDataLoader

  val networkLoader = new NetworkLoaderImpl(cachingExecutor)
  val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)
  val networkAnalyzer = new NetworkAnalyzerImpl(analysisContext, relationAnalyzer, countryAnalyzer, routeAnalyzer)

}
