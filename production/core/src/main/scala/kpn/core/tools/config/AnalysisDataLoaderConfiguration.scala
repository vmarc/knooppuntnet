package kpn.core.tools.config

import java.io.File

import akka.actor.ActorSystem
import kpn.core.engine.analysis.ChangeSetInfoUpdater
import kpn.core.engine.analysis.NetworkAnalyzer
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.ignore.IgnoredNetworkAnalyzer
import kpn.core.engine.changes.ignore.IgnoredNetworkAnalyzerImpl
import kpn.core.engine.changes.ignore.IgnoredNodeAnalyzerImpl
import kpn.core.engine.changes.orphan.node.OrphanNodeCreateProcessorImpl
import kpn.core.engine.changes.orphan.route.OrphanRouteProcessorImpl
import kpn.core.load.AnalysisDataLoader
import kpn.core.load.NetworksLoaderImpl
import kpn.core.load._
import kpn.core.load.orphan.node.NodeIdsLoader
import kpn.core.load.orphan.node.NodeIdsLoaderImpl
import kpn.core.load.orphan.node.OrphanNodesLoaderImpl
import kpn.core.load.orphan.route.OrphanRoutesLoaderImpl
import kpn.core.load.orphan.route.OrphanRoutesLoaderWorkerImpl
import kpn.core.load.orphan.route.RouteIdsLoader
import kpn.core.load.orphan.route.RouteIdsLoaderImpl
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.BlackListRepository
import kpn.core.repository.FactRepository
import kpn.core.repository.OrphanRepository
import kpn.core.tools.analyzer.CouchIndexer

class AnalysisDataLoaderConfiguration(
  system: ActorSystem,
  cacheRootDir: File,
  nonCachingExecutor: OverpassQueryExecutor,
  cachingExecutor: OverpassQueryExecutor,
  analysisData: AnalysisData,
  orphanRepository: OrphanRepository,
  analysisRepository: AnalysisRepository,
  factRepository: FactRepository,
  blackListRepository: BlackListRepository,
  changeSetInfoUpdater: ChangeSetInfoUpdater,
  countryAnalyzer: CountryAnalyzer,
  nodeLoader: NodeLoader,
  analysisDatabaseIndexer: CouchIndexer
) {

  private val nodeIdsLoader: NodeIdsLoader = new NodeIdsLoaderImpl(
    cachingExecutor
  )

  private val routeIdsLoader: RouteIdsLoader = new RouteIdsLoaderImpl(
    cachingExecutor
  )

  private val routeLoader: RouteLoader = new RouteLoaderImpl(
    nonCachingExecutor,
    cachingExecutor,
    countryAnalyzer
  )

  private val networkLoader: NetworkLoader = new NetworkLoaderImpl(
    cachingExecutor
  )

  private val routeAnalyzer = new MasterRouteAnalyzerImpl(new AccessibilityAnalyzerImpl())

  private val orphanRouteProcessor = new OrphanRouteProcessorImpl(
    analysisData,
    analysisRepository,
    countryAnalyzer,
    routeAnalyzer
  )

  val orphanRoutesLoaderWorker = new OrphanRoutesLoaderWorkerImpl(
    routeLoader,
    routeAnalyzer,
    countryAnalyzer,
    analysisData,
    analysisRepository
  )

  private val orphanRoutesLoader = new OrphanRoutesLoaderImpl(
    system,
    analysisData,
    routeIdsLoader,
    orphanRepository,
    blackListRepository,
    analysisDatabaseIndexer,
    orphanRoutesLoaderWorker
  )

  private val orphanNodeCreateProcessor = {
    val ignoredNodeAnalyzer = new IgnoredNodeAnalyzerImpl()
    new OrphanNodeCreateProcessorImpl(
      analysisData,
      analysisRepository,
      ignoredNodeAnalyzer
    )
  }

  private val orphanNodesLoader = new OrphanNodesLoaderImpl(
    analysisData,
    nodeIdsLoader,
    nodeLoader,
    orphanRepository,
    orphanNodeCreateProcessor,
    analysisDatabaseIndexer
  )

  private val networkIdsLoader: NetworkIdsLoader = new NetworkIdsLoaderImpl(
    cachingExecutor
  )

  private val networkRelationAnalyzer: NetworkRelationAnalyzer = new NetworkRelationAnalyzerImpl(
    countryAnalyzer
  )

  private val networkAnalyzer: NetworkAnalyzer = new NetworkAnalyzerImpl(
    countryAnalyzer,
    routeAnalyzer
  )

  private val ignoredNetworkAnalyzer: IgnoredNetworkAnalyzer = new IgnoredNetworkAnalyzerImpl(
    countryAnalyzer
  )

  private val networkInitialLoaderWorker: NetworkInitialLoaderWorker = new NetworkInitialLoaderWorkerImpl(
    analysisRepository,
    analysisData,
    networkLoader,
    networkRelationAnalyzer,
    networkAnalyzer,
    ignoredNetworkAnalyzer,
    blackListRepository
  )

  private val networkInitialLoader: NetworkInitialLoader = new NetworkInitialLoaderImpl(
    system: ActorSystem,
    networkInitialLoaderWorker
  )

  private val networksLoader = new NetworksLoaderImpl(
    networkIdsLoader,
    networkInitialLoader
  )

  val analysisDataLoader = new AnalysisDataLoader(
    analysisData,
    networksLoader,
    orphanRoutesLoader,
    orphanNodesLoader,
    orphanRepository,
    factRepository
  )
}
