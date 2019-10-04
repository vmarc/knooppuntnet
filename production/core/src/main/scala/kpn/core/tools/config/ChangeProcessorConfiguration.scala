package kpn.core.tools.config

import akka.actor.ActorSystem
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
import kpn.core.engine.changes.data.AnalysisData
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
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.BlackListRepository
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NetworkRepository
import kpn.core.repository.TaskRepository

class ChangeProcessorConfiguration(
  system: ActorSystem,
  analysisData: AnalysisData,
  nonCachingExecutor: OverpassQueryExecutor,
  cachingExecutor: OverpassQueryExecutor,
  networkRepository: NetworkRepository,
  analysisRepository: AnalysisRepository,
  countryAnalyzer: CountryAnalyzer,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  taskRepository: TaskRepository,
  blackListRepository: BlackListRepository,
  nodeLoader: NodeLoader
) {

  private val networkLoader = new NetworkLoaderImpl(cachingExecutor)
  private val routeAnalyzer = new MasterRouteAnalyzerImpl(new AccessibilityAnalyzerImpl())
  private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)
  private val networkAnalyzer = new NetworkAnalyzerImpl(countryAnalyzer, routeAnalyzer)

  private val routeLoader = new RouteLoaderImpl(cachingExecutor, countryAnalyzer)

  val nodeChangeBuilder: NodeChangeBuilder = new NodeChangeBuilderImpl(
    analysisData,
    analysisRepository,
    nodeLoader
  )

  val routeChangeBuilder: RouteChangeBuilder = new RouteChangeBuilderImpl(
    analysisData,
    analysisRepository,
    countryAnalyzer,
    routeAnalyzer,
    routeLoader
  )

  private val routesLoader = new RoutesLoaderImpl(
    system,
    routeLoader
  )

  val changeBuilder: ChangeBuilder = new ChangeBuilderImpl(
    analysisData,
    routesLoader,
    countryAnalyzer,
    routeAnalyzer,
    routeChangeBuilder,
    nodeChangeBuilder
  )

  private val networkChangeProcessor = {

    val networkChangeAnalyzer = new NetworkChangeAnalyzerImpl(
      analysisData,
      blackListRepository
    )

    val networkCreateProcessor: NetworkCreateProcessor = {

      val watchedProcessor = new NetworkCreateWatchedProcessorImpl(
        analysisData,
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
        analysisData,
        analysisRepository,
        networkRelationAnalyzer,
        networkAnalyzer,
        changeBuilder
      )

      val worker = new NetworkUpdateProcessorWorkerImpl(
        analysisData,
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
        analysisData,
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
      analysisData,
      analysisRepository,
      countryAnalyzer,
      routeAnalyzer
    )
    val orphanRouteChangeAnalyzer = new OrphanRouteChangeAnalyzer(
      analysisData,
      blackListRepository
    )
    new OrphanRouteChangeProcessorImpl(
      analysisData,
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
      analysisData,
      blackListRepository
    )

    val orphanNodeDeleteProcessor = new OrphanNodeDeleteProcessorImpl(
      analysisData,
      analysisRepository,
      countryAnalyzer
    )

    val orphanNodeCreateProcessor = new OrphanNodeCreateProcessorImpl(
      analysisData,
      analysisRepository
    )

    val orphanNodeUpdateProcessor = new OrphanNodeUpdateProcessorImpl(
      analysisData,
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
