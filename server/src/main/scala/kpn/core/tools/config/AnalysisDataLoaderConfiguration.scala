package kpn.core.tools.config

import java.io.File

import akka.actor.ActorSystem
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.load.AnalysisDataLoader
import kpn.server.analyzer.load.NetworksLoaderImpl
import kpn.server.analyzer.load._
import kpn.server.analyzer.load.orphan.node.NodeIdsLoader
import kpn.server.analyzer.load.orphan.node.NodeIdsLoaderImpl
import kpn.server.analyzer.load.orphan.node.OrphanNodesLoaderImpl
import kpn.server.analyzer.load.orphan.route.OrphanRoutesLoaderImpl
import kpn.server.analyzer.load.orphan.route.OrphanRoutesLoaderWorkerImpl
import kpn.server.analyzer.load.orphan.route.RouteIdsLoader
import kpn.server.analyzer.load.orphan.route.RouteIdsLoaderImpl
import kpn.core.overpass.OverpassQueryExecutor
import kpn.server.analyzer.engine.AnalysisContext
import kpn.server.analyzer.engine.CouchIndexer
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.analysis.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.NetworkRelationAnalyzer
import kpn.server.analyzer.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeCreateProcessorImpl
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteProcessorImpl
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.BlackListRepository
import kpn.server.repository.FactRepository
import kpn.server.repository.OrphanRepository

class AnalysisDataLoaderConfiguration(
  system: ActorSystem,
  analysisContext: AnalysisContext,
  cacheRootDir: File,
  nonCachingExecutor: OverpassQueryExecutor,
  cachingExecutor: OverpassQueryExecutor,
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

  private val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl())

  private val orphanRouteProcessor = new OrphanRouteProcessorImpl(
    analysisContext,
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
    analysisRepository
  )

  private val orphanRoutesLoader = new OrphanRoutesLoaderImpl(
    system,
    analysisContext,
    routeIdsLoader,
    orphanRepository,
    blackListRepository,
    analysisDatabaseIndexer,
    orphanRoutesLoaderWorker
  )

  private val orphanNodeCreateProcessor = {
    new OrphanNodeCreateProcessorImpl(
      analysisContext,
      analysisRepository
    )
  }

  private val orphanNodesLoader = new OrphanNodesLoaderImpl(
    analysisContext,
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
    analysisContext,
    analysisRepository,
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
    analysisContext,
    networksLoader,
    orphanRoutesLoader,
    orphanNodesLoader
  )
}
