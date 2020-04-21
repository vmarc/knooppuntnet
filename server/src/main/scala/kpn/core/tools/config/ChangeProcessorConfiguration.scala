package kpn.core.tools.config

import java.util.concurrent.Executor

import kpn.core.overpass.OverpassQueryExecutor
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSaver
import kpn.server.analyzer.engine.changes.ChangeSaverImpl
import kpn.server.analyzer.engine.changes.builder.ChangeBuilder
import kpn.server.analyzer.engine.changes.builder.ChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilder
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilder
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilderImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.network.NetworkChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessorImpl
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessor
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessorImpl
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessorWorkerImpl
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateWatchedProcessorImpl
import kpn.server.analyzer.engine.changes.network.delete.NetworkDeleteProcessorImpl
import kpn.server.analyzer.engine.changes.network.delete.NetworkDeleteProcessorWorkerImpl
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateNetworkProcessorImpl
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessor
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessorImpl
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessorWorkerImpl
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeChangeProcessorImpl
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeCreateProcessorImpl
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeDeleteProcessorImpl
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeUpdateProcessorImpl
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteChangeAnalyzer
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteChangeProcessorImpl
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteProcessor
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteProcessorImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileChangeAnalyzerImpl
import kpn.server.analyzer.load.NetworkLoaderImpl
import kpn.server.analyzer.load.NodeLoader
import kpn.server.analyzer.load.RouteLoaderImpl
import kpn.server.analyzer.load.RoutesLoaderImpl
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.BlackListRepository
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeInfoBuilderImpl
import kpn.server.repository.TaskRepository

class ChangeProcessorConfiguration(
  executor: Executor,
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

  private val tileCalculator = new TileCalculatorImpl()
  private val nodeTileAnalyzer = new NodeTileAnalyzerImpl(tileCalculator)
  private val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
  private val nodeInfoBuilder = new NodeInfoBuilderImpl(nodeTileAnalyzer, null)
  private val networkLoader = new NetworkLoaderImpl(cachingExecutor)
  private val routeAnalyzer = new MasterRouteAnalyzerImpl(
    analysisContext,
    new AccessibilityAnalyzerImpl(),
    routeTileAnalyzer
  )
  private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)
  private val networkAnalyzer = new NetworkAnalyzerImpl(analysisContext, relationAnalyzer, countryAnalyzer, routeAnalyzer)

  private val routeLoader = new RouteLoaderImpl(cachingExecutor, countryAnalyzer)

  val nodeChangeBuilder: NodeChangeBuilder = new NodeChangeBuilderImpl(
    analysisContext,
    analysisRepository,
    nodeLoader,
    nodeInfoBuilder
  )

  val tileChangeAnalyzer = new TileChangeAnalyzerImpl(taskRepository, routeTileAnalyzer)

  val routeChangeBuilder: RouteChangeBuilder = new RouteChangeBuilderImpl(
    analysisContext,
    analysisRepository,
    relationAnalyzer,
    countryAnalyzer,
    routeAnalyzer,
    routeLoader,
    tileChangeAnalyzer
  )

  private val routesLoader = new RoutesLoaderImpl(
    executor,
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

      new NetworkCreateProcessorImpl(
        executor,
        new NetworkCreateProcessorWorkerImpl(
          networkLoader,
          networkRelationAnalyzer,
          watchedProcessor
        )
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

      new NetworkUpdateProcessorImpl(
        executor,
        new NetworkUpdateProcessorWorkerImpl(
          analysisRepository,
          networkLoader,
          networkRelationAnalyzer,
          networkUpdateNetworkProcessor
        )
      )
    }

    val networkDeleteProcessor = {
      new NetworkDeleteProcessorImpl(
        executor,
        new NetworkDeleteProcessorWorkerImpl(
          analysisContext,
          networkRepository,
          networkLoader,
          networkRelationAnalyzer,
          networkAnalyzer,
          changeBuilder
        )
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
      routeAnalyzer,
      nodeInfoBuilder
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
      countryAnalyzer,
      tileChangeAnalyzer
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
      countryAnalyzer,
      nodeInfoBuilder
    )

    val orphanNodeCreateProcessor = new OrphanNodeCreateProcessorImpl(
      analysisContext,
      analysisRepository,
      nodeInfoBuilder
    )

    val orphanNodeUpdateProcessor = new OrphanNodeUpdateProcessorImpl(
      analysisContext,
      analysisRepository,
      nodeInfoBuilder
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
