package kpn.core.tools.config

import java.io.File

import akka.actor.ActorSystem
import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.ChangeSetInfoUpdater
import kpn.core.engine.analysis.NetworkAnalyzer
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.engine.changes.data.AnalysisData
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
import kpn.core.tools.analyzer.AnalysisContext
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
  relationAnalyzer: RelationAnalyzer,
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
    cachingExecutor,
    countryAnalyzer
  )

  private val networkLoader: NetworkLoader = new NetworkLoaderImpl(
    cachingExecutor
  )

  private val analysisContext = new AnalysisContext()

  private val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl())

  private val orphanRouteProcessor = new OrphanRouteProcessorImpl(
    analysisContext,
    analysisData,
    analysisRepository,
    relationAnalyzer,
    countryAnalyzer,
    routeAnalyzer
  )

  val orphanRoutesLoaderWorker = new OrphanRoutesLoaderWorkerImpl(
    analysisContext,
    routeLoader,
    routeAnalyzer,
    relationAnalyzer,
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
    new OrphanNodeCreateProcessorImpl(
      analysisData,
      analysisRepository
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
    relationAnalyzer,
    countryAnalyzer
  )

  private val networkAnalyzer: NetworkAnalyzer = new NetworkAnalyzerImpl(
    analysisContext,
    relationAnalyzer,
    countryAnalyzer,
    routeAnalyzer
  )

  private val networkInitialLoaderWorker: NetworkInitialLoaderWorker = new NetworkInitialLoaderWorkerImpl(
    analysisRepository,
    analysisData,
    networkLoader,
    networkRelationAnalyzer,
    networkAnalyzer,
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
