package kpn.core.tools.config

import akka.actor.ActorSystem
import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.engine.changes.ChangeProcessor
import kpn.core.engine.changes.ChangeSaver
import kpn.core.engine.changes.ChangeSaverImpl
import kpn.core.engine.changes.builder.ChangeBuilder
import kpn.core.engine.changes.builder.ChangeBuilderImpl
import kpn.core.engine.changes.builder.NodeChangeBuilder
import kpn.core.engine.changes.builder.NodeChangeBuilderImpl
import kpn.core.engine.changes.builder.RouteChangeBuilder
import kpn.core.engine.changes.builder.RouteChangeBuilderImpl
import kpn.core.engine.changes.network.NetworkChangeAnalyzerImpl
import kpn.core.engine.changes.network.NetworkChangeProcessorImpl
import kpn.core.engine.changes.network.create.NetworkCreateProcessor
import kpn.core.engine.changes.network.create.NetworkCreateProcessorImpl
import kpn.core.engine.changes.network.create.NetworkCreateProcessorWorkerImpl
import kpn.core.engine.changes.network.create.NetworkCreateWatchedProcessorImpl
import kpn.core.engine.changes.network.delete.NetworkDeleteProcessorImpl
import kpn.core.engine.changes.network.delete.NetworkDeleteProcessorWorkerImpl
import kpn.core.engine.changes.network.update.NetworkUpdateNetworkProcessorImpl
import kpn.core.engine.changes.network.update.NetworkUpdateProcessor
import kpn.core.engine.changes.network.update.NetworkUpdateProcessorImpl
import kpn.core.engine.changes.network.update.NetworkUpdateProcessorWorkerImpl
import kpn.core.engine.changes.orphan.node.OrphanNodeChangeAnalyzerImpl
import kpn.core.engine.changes.orphan.node.OrphanNodeChangeProcessorImpl
import kpn.core.engine.changes.orphan.node.OrphanNodeCreateProcessorImpl
import kpn.core.engine.changes.orphan.node.OrphanNodeDeleteProcessorImpl
import kpn.core.engine.changes.orphan.node.OrphanNodeUpdateProcessorImpl
import kpn.core.engine.changes.orphan.route.OrphanRouteChangeAnalyzer
import kpn.core.engine.changes.orphan.route.OrphanRouteChangeProcessorImpl
import kpn.core.engine.changes.orphan.route.OrphanRouteProcessor
import kpn.core.engine.changes.orphan.route.OrphanRouteProcessorImpl
import kpn.core.load.NetworkLoaderImpl
import kpn.core.load.NodeLoader
import kpn.core.load.RouteLoaderImpl
import kpn.core.load.RoutesLoaderImpl
import kpn.core.overpass.OverpassQueryExecutor
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.BlackListRepository
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.TaskRepository
import kpn.core.tools.analyzer.AnalysisContext

class ChangeProcessorConfiguration(
  system: ActorSystem,
  analysisContext: AnalysisContext,
  nonCachingExecutor: OverpassQueryExecutor,
  cachingExecutor: OverpassQueryExecutor,
  networkRepository: NetworkRepository,
  analysisRepository: AnalysisRepository,
  relationAnalyzer: RelationAnalyzer,
  countryAnalyzer: CountryAnalyzer,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  taskRepository: TaskRepository,
  blackListRepository: BlackListRepository,
  nodeLoader: NodeLoader
) {

  private val networkLoader = new NetworkLoaderImpl(cachingExecutor)
  private val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl())
  private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)
  private val networkAnalyzer = new NetworkAnalyzerImpl(analysisContext, relationAnalyzer, countryAnalyzer, routeAnalyzer)

  private val routeLoader = new RouteLoaderImpl(cachingExecutor, countryAnalyzer)

  val nodeChangeBuilder: NodeChangeBuilder = new NodeChangeBuilderImpl(
    analysisContext,
    analysisRepository,
    nodeLoader
  )

  val routeChangeBuilder: RouteChangeBuilder = new RouteChangeBuilderImpl(
    analysisContext,
    analysisRepository,
    relationAnalyzer,
    countryAnalyzer,
    routeAnalyzer,
    routeLoader
  )

  private val routesLoader = new RoutesLoaderImpl(
    system,
    routeLoader
  )

  val changeBuilder: ChangeBuilder = new ChangeBuilderImpl(
    analysisContext,
    routesLoader,
    countryAnalyzer,
    routeAnalyzer,
    routeChangeBuilder,
    nodeChangeBuilder
  )

  private val networkChangeProcessor = {

    val networkChangeAnalyzer = new NetworkChangeAnalyzerImpl(
      analysisContext,
      blackListRepository
    )

    val networkCreateProcessor: NetworkCreateProcessor = {

      val watchedProcessor = new NetworkCreateWatchedProcessorImpl(
        analysisContext,
        analysisRepository,
        networkRelationAnalyzer,
        networkAnalyzer,
        changeBuilder
      )

      val worker = new NetworkCreateProcessorWorkerImpl(
        networkLoader,
        networkRelationAnalyzer,
        watchedProcessor
      )

      new NetworkCreateProcessorImpl(
        system,
        worker
      )
    }

    val networkUpdateProcessor: NetworkUpdateProcessor = {

      val networkUpdateNetworkProcessor = new NetworkUpdateNetworkProcessorImpl(
        analysisContext,
        analysisRepository,
        networkRelationAnalyzer,
        networkAnalyzer,
        changeBuilder
      )

      val worker = new NetworkUpdateProcessorWorkerImpl(
        analysisRepository,
        networkLoader,
        networkRelationAnalyzer,
        networkUpdateNetworkProcessor
      )

      new NetworkUpdateProcessorImpl(
        system,
        worker
      )
    }

    val networkDeleteProcessor = {

      val worker = new NetworkDeleteProcessorWorkerImpl(
        analysisContext,
        networkRepository,
        networkLoader,
        networkRelationAnalyzer,
        networkAnalyzer,
        changeBuilder
      )

      new NetworkDeleteProcessorImpl(
        system,
        worker
      )
    }

    new NetworkChangeProcessorImpl(
      networkChangeAnalyzer,
      networkCreateProcessor,
      networkUpdateProcessor,
      networkDeleteProcessor
    )
  }

  private val changeSetInfoUpdater = new ChangeSetInfoUpdaterImpl(
    changeSetInfoRepository,
    taskRepository
  )

  private val orphanRouteChangeProcessor = {
    val orphanRouteProcessor: OrphanRouteProcessor = new OrphanRouteProcessorImpl(
      analysisContext,
      analysisRepository,
      relationAnalyzer,
      countryAnalyzer,
      routeAnalyzer
    )
    val orphanRouteChangeAnalyzer = new OrphanRouteChangeAnalyzer(
      analysisContext,
      blackListRepository
    )
    new OrphanRouteChangeProcessorImpl(
      analysisContext,
      analysisRepository,
      orphanRouteChangeAnalyzer,
      orphanRouteProcessor,
      routesLoader,
      routeAnalyzer,
      countryAnalyzer
    )
  }

  private val orphanNodeChangeProcessor = {

    val orphanNodeChangeAnalyzer = new OrphanNodeChangeAnalyzerImpl(
      analysisContext,
      blackListRepository
    )

    val orphanNodeDeleteProcessor = new OrphanNodeDeleteProcessorImpl(
      analysisContext,
      analysisRepository,
      countryAnalyzer
    )

    val orphanNodeCreateProcessor = new OrphanNodeCreateProcessorImpl(
      analysisContext,
      analysisRepository
    )

    val orphanNodeUpdateProcessor = new OrphanNodeUpdateProcessorImpl(
      analysisContext,
      analysisRepository
    )

    new OrphanNodeChangeProcessorImpl(
      orphanNodeChangeAnalyzer,
      orphanNodeCreateProcessor,
      orphanNodeUpdateProcessor,
      orphanNodeDeleteProcessor,
      countryAnalyzer,
      nodeLoader
    )
  }

  val changeProcessor: ChangeProcessor = {

    val changeSaver: ChangeSaver = new ChangeSaverImpl(
      changeSetRepository: ChangeSetRepository
    )

    new ChangeProcessor(
      changeSetRepository,
      networkChangeProcessor,
      orphanRouteChangeProcessor,
      orphanNodeChangeProcessor,
      changeSetInfoUpdater,
      changeSaver
    )
  }
}
