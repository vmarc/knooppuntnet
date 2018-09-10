package kpn.core.tools

import akka.actor.ActorSystem
import kpn.core.changes.ChangeSetInfoApiImpl
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.engine.analysis.ChangeSetInfoUpdater
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.engine.changes.OsmChangeRepository
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.AnalysisDataLoader
import kpn.core.load.NetworkLoaderImpl
import kpn.core.load.NodeLoaderImpl
import kpn.core.load.RouteLoader
import kpn.core.load.RouteLoaderImpl
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.core.repository.AnalysisDataRepository
import kpn.core.repository.AnalysisDataRepositoryImpl
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.AnalysisRepositoryImpl
import kpn.core.repository.BlackListRepositoryImpl
import kpn.core.repository.ChangeSetInfoRepositoryImpl
import kpn.core.repository.ChangeSetRepositoryImpl
import kpn.core.repository.FactRepositoryImpl
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.repository.NodeRepositoryImpl
import kpn.core.repository.OrphanRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl
import kpn.core.tools.analyzer.CouchIndexer
import kpn.core.tools.config.AnalysisDataLoaderConfiguration
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl

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

  val countryAnalyzer = new CountryAnalyzerImpl()

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
    cachingExecutor,
    nonCachingExecutor,
    countryAnalyzer
  )

  val routeLoader: RouteLoader = new RouteLoaderImpl(
    cachingExecutor,
    countryAnalyzer
  )

  val routeAnalyzer = new MasterRouteAnalyzerImpl(new AccessibilityAnalyzerImpl())

  val analysisDataLoader: AnalysisDataLoader = new AnalysisDataLoaderConfiguration(
    system,
    dirs.cache,
    nonCachingExecutor,
    cachingExecutor,
    analysisData,
    orphanRepository,
    analysisRepository,
    factRepository,
    blackListRepository,
    changeSetInfoUpdater,
    countryAnalyzer,
    nodeLoader,
    analysisDatabaseIndexer
  ).analysisDataLoader

  val networkLoader = new NetworkLoaderImpl(cachingExecutor)
  val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)
  val networkAnalyzer = new NetworkAnalyzerImpl(countryAnalyzer, routeAnalyzer)

}
