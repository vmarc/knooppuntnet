package kpn.core.tools.config

import akka.actor.ActorSystem
import kpn.core.changes.ChangeSetInfoApiImpl
import kpn.core.changes.RelationAnalyzerImpl
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.engine.changes.ChangeProcessor
import kpn.core.engine.changes.OsmChangeRepository
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.AnalysisDataLoader
import kpn.core.load.NodeLoaderImpl
import kpn.core.load.RouteLoader
import kpn.core.load.RouteLoaderImpl
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.core.repository.AnalysisDataRepository
import kpn.core.repository.AnalysisDataRepositoryImpl
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.BlackListRepositoryImpl
import kpn.core.repository.ChangeSetInfoRepositoryImpl
import kpn.core.repository.ChangeSetRepositoryImpl
import kpn.core.repository.FactRepositoryImpl
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.repository.OrphanRepositoryImpl
import kpn.core.repository.TaskRepositoryImpl
import kpn.core.tools.analyzer.AnalysisContext
import kpn.core.tools.analyzer.CouchIndexer
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.shared.ReplicationId

object Configuration {
  def tempAnalysisSpeedUp: Boolean = true
}

class Configuration(
  id: Int,
  system: ActorSystem,
  analysisDatabase: Database,
  changeDatabase: Database,
  taskDatabase: Database,
  val analysisRepository: AnalysisRepository,
  initialLoadAnalysisRepository: AnalysisRepository
) {

  val dirs = Dirs()

  val statusRepository: StatusRepository = new StatusRepositoryImpl(dirs)

  def analysisStatus: Option[ReplicationId] = {
    id match {
      case 1 => statusRepository.analysisStatus1
      case 2 => statusRepository.analysisStatus2
      case 3 => statusRepository.analysisStatus3
      case _ => throw new IllegalArgumentException("unexpected analyzer id " + id)
    }
  }

  def analysisStatusPath: String = {
    id match {
      case 1 => dirs.analysisStatus1.getAbsolutePath
      case 2 => dirs.analysisStatus2.getAbsolutePath
      case 3 => dirs.analysisStatus3.getAbsolutePath
      case _ => throw new IllegalArgumentException("unexpected analyzer id " + id)
    }
  }

  def writeAnalysisStatus(replicationId: ReplicationId): Unit = {
    id match {
      case 1 => statusRepository.writeAnalysisStatus1(replicationId)
      case 2 => statusRepository.writeAnalysisStatus2(replicationId)
      case 3 => statusRepository.writeAnalysisStatus3(replicationId)
      case _ => throw new IllegalArgumentException("unexpected analyzer id " + id)
    }
  }

  val changeSetInfoApi = new ChangeSetInfoApiImpl(dirs.changeSets, system)

  val nonCachingExecutor = new OverpassQueryExecutorWithThrotteling(system, new OverpassQueryExecutorImpl())

  val cachingExecutor = new CachingOverpassQueryExecutor(dirs.cache, nonCachingExecutor)

  val osmChangeRepository = new OsmChangeRepository(dirs.replicate)

  val networkRepository = new NetworkRepositoryImpl(analysisDatabase)

  val analysisDataRepository: AnalysisDataRepository = new AnalysisDataRepositoryImpl(analysisDatabase)

  val analysisDatabaseIndexer: CouchIndexer = new CouchIndexer(
    analysisDatabase, AnalyzerDesign
  )

  val analysisData: AnalysisData = AnalysisData()

  private val analysisContext = new AnalysisContext()

  private val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)

  private val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)

  private val changeSetRepository = new ChangeSetRepositoryImpl(
    changeDatabase
  )

  val taskRepository = new TaskRepositoryImpl(taskDatabase)

  val changeSetInfoRepository = new ChangeSetInfoRepositoryImpl(
    changeDatabase
  )

  private val blackListRepository = new BlackListRepositoryImpl(
    analysisDatabase
  )

  val changeSetInfoUpdater = new ChangeSetInfoUpdaterImpl(
    changeSetInfoRepository,
    taskRepository
  )

  val orphanRepository = new OrphanRepositoryImpl(
    analysisDatabase
  )

  val factRepository = new FactRepositoryImpl(
    analysisDatabase
  )

  private val nodeLoader = new NodeLoaderImpl(
    nonCachingExecutor,
    cachingExecutor,
    countryAnalyzer
  )

  private val routeLoader: RouteLoader = new RouteLoaderImpl(
    cachingExecutor,
    countryAnalyzer
  )

  private val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl())

  val changeProcessor: ChangeProcessor = new ChangeProcessorConfiguration(
    system,
    analysisContext,
    nonCachingExecutor,
    cachingExecutor,
    networkRepository,
    analysisRepository,
    relationAnalyzer,
    countryAnalyzer,
    changeSetRepository,
    changeSetInfoRepository,
    taskRepository,
    blackListRepository,
    nodeLoader
  ).changeProcessor

  val analysisDataLoader: AnalysisDataLoader = new AnalysisDataLoaderConfiguration(
    system,
    analysisContext,
    dirs.cache,
    nonCachingExecutor,
    cachingExecutor,
    orphanRepository,
    initialLoadAnalysisRepository,
    factRepository,
    blackListRepository,
    changeSetInfoUpdater,
    relationAnalyzer,
    countryAnalyzer,
    nodeLoader,
    analysisDatabaseIndexer
  ).analysisDataLoader

  {
    val availableProcessors = Runtime.getRuntime.availableProcessors
    val maxThreads = if (availableProcessors > 2) {
      availableProcessors - 2
    }
    else {
      1
    }

    System.setProperty("scala.concurrent.context.maxThreads", "" + 4 /*maxThreads*/)
  }
}
