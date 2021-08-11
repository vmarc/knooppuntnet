package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Change
import kpn.api.custom.Country
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.Data
import kpn.core.loadOld.OsmDataXmlWriter
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.mongo.doc.NodeDoc
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNode
import kpn.core.overpass.QueryNodes
import kpn.core.overpass.QueryRelation
import kpn.core.test.OverpassData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoChangeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoFactAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoNodeAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkInfoTagAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.OldMainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSaverImpl
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementIdAnalyzerSyncImpl
import kpn.server.analyzer.engine.changes.builder.ChangeBuilder
import kpn.server.analyzer.engine.changes.builder.ChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilder
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilder
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilderImpl
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.data.BlackList
import kpn.server.analyzer.engine.changes.network.NetworkChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessorImpl
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessor
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessorSyncImpl
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessorWorkerImpl
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateWatchedProcessorImpl
import kpn.server.analyzer.engine.changes.network.delete.NetworkDeleteProcessorSyncImpl
import kpn.server.analyzer.engine.changes.network.delete.NetworkDeleteProcessorWorkerImpl
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateNetworkProcessorImpl
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessor
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessorSyncImpl
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessorWorkerImpl
import kpn.server.analyzer.engine.changes.node.NewNodeChangeProcessorImpl
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessorImpl
import kpn.server.analyzer.engine.changes.route.create.RouteCreateProcessor
import kpn.server.analyzer.engine.changes.route.delete.RouteDeleteProcessor
import kpn.server.analyzer.engine.changes.route.update.RouteUpdateProcessor
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileChangeAnalyzerImpl
import kpn.server.analyzer.full.FullAnalyzerImpl
import kpn.server.analyzer.full.network.FullNetworkAnalyzer
import kpn.server.analyzer.full.network.FullNetworkAnalyzerImpl
import kpn.server.analyzer.full.node.FullNodeAnalyzer
import kpn.server.analyzer.full.node.FullNodeAnalyzerImpl
import kpn.server.analyzer.full.route.FullRouteAnalyzer
import kpn.server.analyzer.full.route.FullRouteAnalyzerImpl
import kpn.server.analyzer.load.AnalysisDataInitializer
import kpn.server.analyzer.load.AnalysisDataInitializerImpl
import kpn.server.analyzer.load.NetworkLoader
import kpn.server.analyzer.load.NetworkLoaderImpl
import kpn.server.analyzer.load.NodeLoader
import kpn.server.analyzer.load.NodeLoaderImpl
import kpn.server.analyzer.load.OldRouteLoader
import kpn.server.analyzer.load.OldRouteLoaderImpl
import kpn.server.analyzer.load.RoutesLoaderSyncImpl
import kpn.server.analyzer.load.data.RawDataSplitter
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.AnalysisRepositoryImpl
import kpn.server.repository.BlackListRepository
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory

import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

abstract class AbstractIntegrationTest extends UnitTest with MockFactory with SharedTestObjects {

  protected def node(data: Data, id: Long): RawNode = {
    data.raw.nodeWithId(id).get
  }

  protected def relation(data: Data, id: Long): RawRelation = {
    data.raw.relationWithId(id).get
  }

  protected class OldTestConfig(dataBefore: Data, dataAfter: Data) {

    val analysisContext = new AnalysisContext()

    val overpassRepository: OverpassRepository = new OverpassRepositoryMock(dataBefore, dataAfter)
    val analysisExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

    val countryAnalyzer: CountryAnalyzer = new CountryAnalyzerMock()
    val oldNodeAnalyzer: OldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val overpassQueryExecutor: OverpassQueryExecutor = stub[OverpassQueryExecutor]

    val changeSetRepository: ChangeSetRepository = stub[ChangeSetRepository]
    val nodeRepository: NodeRepository = stub[NodeRepository]
    (nodeRepository.nodeRouteReferences _).when(*, *).returns(Seq.empty)

    val routeRepository: RouteRepository = stub[RouteRepository]
    val networkRepository: NetworkRepository = stub[NetworkRepository]
    val changeSetInfoRepository: ChangeSetInfoRepository = stub[ChangeSetInfoRepository]
    private val taskRepository: TaskRepository = stub[TaskRepository]

    private val blackListRepository: BlackListRepository = stub[BlackListRepository]
    (() => blackListRepository.get).when().returns(BlackList())

    private val nodeLoader = new NodeLoaderImpl(overpassQueryExecutor, countryAnalyzer, oldNodeAnalyzer)
    private val routeLoader = new OldRouteLoaderImpl(overpassQueryExecutor)
    private val networkLoader: NetworkLoader = new NetworkLoaderImpl(overpassQueryExecutor)
    private val tileCalculator = new TileCalculatorImpl()
    private val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
    private val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    private val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val masterRouteAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)

    private val elementIdAnalyzer = new ElementIdAnalyzerSyncImpl()

    private val oldNodeLocationAnalyzer = stub[OldNodeLocationAnalyzer]
    (oldNodeLocationAnalyzer.locations _).when(*, *).returns(Seq.empty)
    (oldNodeLocationAnalyzer.oldLocate _).when(*, *).returns(None)

    val oldMainNodeAnalyzer = new OldMainNodeAnalyzerImpl(
      countryAnalyzer,
      oldNodeLocationAnalyzer
    )

    val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(oldMainNodeAnalyzer, oldNodeAnalyzer)

    val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
      countryAnalyzer,
      masterRouteAnalyzer
    )

    private val networkAnalyzer = new NetworkAnalyzerImpl(
      networkNodeAnalyzer,
      networkRouteAnalyzer
    )

    private val nodeAnalyzer: NodeAnalyzer = {
      new NodeAnalyzerImpl(
        new NodeCountryAnalyzerMock(Some(Country.nl)),
        new NodeTileAnalyzerNoop,
        new NodeLocationsAnalyzerNoop,
        new NodeRouteReferencesAnalyzerNoop
      )
    }

    val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(
      null,
      networkRepository,
      routeRepository,
      nodeRepository,
      nodeAnalyzer
    )

    private val nodeChangeBuilder: NodeChangeBuilder = new NodeChangeBuilderImpl(
      analysisContext,
      nodeRepository,
      nodeLoader,
      nodeAnalyzer
    )

    private val tileChangeAnalyzer = new TileChangeAnalyzerImpl(
      taskRepository,
      routeTileCalculator
    )

    private val routeChangeBuilder: RouteChangeBuilder = new RouteChangeBuilderImpl(
      analysisContext,
      routeRepository,
      tileChangeAnalyzer
    )

    private val routesLoader = new RoutesLoaderSyncImpl(
      routeLoader
    )

    val changeBuilder: ChangeBuilder = new ChangeBuilderImpl(
      analysisContext,
      routesLoader,
      masterRouteAnalyzer,
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
          watchedProcessor
        )

        new NetworkCreateProcessorSyncImpl(
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
          networkLoader,
          networkUpdateNetworkProcessor
        )

        new NetworkUpdateProcessorSyncImpl(
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

        new NetworkDeleteProcessorSyncImpl(
          worker
        )
      }

      new NetworkChangeProcessorImpl(
        networkChangeAnalyzer,
        //  networkCreateProcessor,
        //  networkUpdateProcessor,
        //  networkDeleteProcessor,
        null
      )
    }

    private val changeSetInfoUpdater = new ChangeSetInfoUpdaterImpl(
      changeSetInfoRepository,
      taskRepository
    )

    private val routeChangeProcessor: RouteChangeProcessor = {

      val routeChangeAnalyzer = new RouteChangeAnalyzer(
        analysisContext,
        blackListRepository,
        elementIdAnalyzer
      )

      val createProcessor: RouteCreateProcessor = null
      val updateProcessor: RouteUpdateProcessor = null
      val deleteProcessor: RouteDeleteProcessor = null

      new RouteChangeProcessorImpl(
        analysisContext,
        routeChangeAnalyzer,
        createProcessor,
        updateProcessor,
        deleteProcessor,
        overpassRepository,
        masterRouteAnalyzer,
        tileChangeAnalyzer,
        routeRepository,
        analysisExecutionContext
      )
    }

    private val nodeChangeProcessor = {

      val nodeChangeAnalyzer: NodeChangeAnalyzer = new NodeChangeAnalyzerImpl(
        analysisContext,
        blackListRepository
      )

      val bulkNodeAnalyzer: BulkNodeAnalyzer = new BulkNodeAnalyzerImpl(
        null,
        overpassRepository,
        nodeAnalyzer
      )

      new NewNodeChangeProcessorImpl(
        analysisContext,
        bulkNodeAnalyzer,
        nodeChangeAnalyzer,
        nodeRepository
      )
    }


    val changeProcessor: ChangeProcessor = {
      val changeSaver = new ChangeSaverImpl(
        changeSetRepository
      )

      new ChangeProcessor(
        routeChangeProcessor,
        networkChangeProcessor,
        null,
        nodeChangeProcessor,
        changeSetInfoUpdater,
        changeSaver
      )
    }

    def process(action: ChangeAction, element: RawElement): Unit = {
      val changes = Seq(Change(action, Seq(element)))
      process(changes)
    }

    def process(changes: Seq[Change]): Unit = {
      val changeSet = newChangeSet(changes = changes)
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val changeSetContext = ChangeSetContext(ReplicationId(1), changeSet, elementIds)
      changeProcessor.process(changeSetContext)
    }

    @deprecated
    def relationBefore(data: Data, relationId: Long): Unit = {
      overpassQueryRelation(data, timestampBeforeValue, relationId)
    }

    @deprecated
    def relationAfter(data: Data, relationId: Long): Unit = {
      overpassQueryRelation(data, timestampAfterValue, relationId)
    }

    @deprecated
    def nodeBefore(data: Data, nodeId: Long): Unit = {
      overpassQueryNode(data, timestampBeforeValue, nodeId)
    }

    @deprecated
    def nodeAfter(data: Data, nodeId: Long): Unit = {
      overpassQueryNode(data, timestampAfterValue, nodeId)
    }

    @deprecated
    def nodesBefore(data: Data, nodeIds: Long*): Unit = {
      overpassQueryNodes(data, timestampBeforeValue, nodeIds)
    }

    @deprecated
    def nodesAfter(data: Data, nodeIds: Long*): Unit = {
      overpassQueryNodes(data, timestampAfterValue, nodeIds)
    }

    def watchNetwork(data: Data, networkId: Long): Unit = {
      analysisContext.data.networks.watched.add(networkId, RelationAnalyzer.toElementIds(data.relations(networkId)))
    }

    def watchOrphanRoute(data: Data, routeId: Long): Unit = {
      val elementIds = RelationAnalyzer.toElementIds(data.relations(routeId))
      analysisContext.data.routes.watched.add(routeId, elementIds)
    }

    def watchOrphanNode(nodeId: Long): Unit = {
      analysisContext.data.nodes.watched.add(nodeId)
    }

    private def overpassQueryRelation(data: Data, timestamp: Timestamp, relationId: Long): Unit = {
      val xml = toXml(RawDataSplitter.extractRelation(data.raw, relationId))
      overpassQuery(timestamp, QueryRelation(relationId), xml)
    }

    private def overpassQueryNode(data: Data, timestamp: Timestamp, nodeId: Long): Unit = {
      val xml = toXml(RawData(Some(timestamp), data.raw.nodeWithId(nodeId).toSeq))
      overpassQuery(timestamp, QueryNode(nodeId), xml)
    }

    def overpassQueryNodes(data: Data, timestamp: Timestamp, nodeIds: Seq[Long]): Unit = {
      val nodes = nodeIds.flatMap(nodeId => data.raw.nodeWithId(nodeId))
      val xml = toXml(RawData(Some(timestamp), nodes))
      overpassQuery(timestamp, QueryNodes("nodes", nodeIds), xml)
    }

    private def overpassQuery(timestamp: Timestamp, query: OverpassQuery, xml: String): Unit = {
      (overpassQueryExecutor.executeQuery _).when(Some(timestamp), query).returns(xml).anyNumberOfTimes()
      ()
    }

    private def toXml(data: RawData): String = {
      val sw = new StringWriter()
      val pw = new PrintWriter(sw)
      new OsmDataXmlWriter(data, pw).print()
      sw.toString
    }
  }

  protected class IntegrationTestContext(database: Database, dataBefore: OverpassData, dataAfter: OverpassData) {

    val before: Data = dataBefore.data
    val after: Data = dataAfter.data

    val analysisContext = new AnalysisContext()

    val overpassRepository: OverpassRepository = new OverpassRepositoryMock(before, after)
    val analysisExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

    val countryAnalyzer: CountryAnalyzer = new CountryAnalyzerMock()
    val oldNodeAnalyzer: OldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val overpassQueryExecutor: OverpassQueryExecutor = stub[OverpassQueryExecutor]

    val changeSetRepository: ChangeSetRepository = new ChangeSetRepositoryImpl(database, null, true)
    val nodeRepository: NodeRepository = new NodeRepositoryImpl(database, null, true)
    val routeRepository: RouteRepository = new RouteRepositoryImpl(database, null, true)
    val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database, null, true)
    val changeSetInfoRepository: ChangeSetInfoRepository = new ChangeSetInfoRepositoryImpl(database, null, true)

    private val taskRepository: TaskRepository = stub[TaskRepository]
    private val blackListRepository: BlackListRepository = stub[BlackListRepository]
    (() => blackListRepository.get).when().returns(BlackList())

    private val nodeLoader: NodeLoader = null
    private val routeLoader: OldRouteLoader = null
    private val networkLoader: NetworkLoader = null
    private val tileCalculator = new TileCalculatorImpl()
    private val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
    private val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    private val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val masterRouteAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)

    private val elementIdAnalyzer = new ElementIdAnalyzerSyncImpl()

    private val oldNodeLocationAnalyzer = stub[OldNodeLocationAnalyzer]
    (oldNodeLocationAnalyzer.locations _).when(*, *).returns(Seq.empty)
    (oldNodeLocationAnalyzer.oldLocate _).when(*, *).returns(None)

    val oldMainNodeAnalyzer = new OldMainNodeAnalyzerImpl(
      countryAnalyzer,
      oldNodeLocationAnalyzer
    )

    val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(oldMainNodeAnalyzer, oldNodeAnalyzer)

    val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
      countryAnalyzer,
      masterRouteAnalyzer
    )

    private val networkAnalyzer = new NetworkAnalyzerImpl(
      networkNodeAnalyzer,
      networkRouteAnalyzer
    )

    private val nodeAnalyzer: NodeAnalyzer = {
      val nodeCountryAnalyzer = new NodeCountryAnalyzerImpl(countryAnalyzer)
      new NodeAnalyzerImpl(
        nodeCountryAnalyzer,
        new NodeTileAnalyzerNoop,
        new NodeLocationsAnalyzerNoop,
        new NodeRouteReferencesAnalyzerNoop
      )
    }

    val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(
      null,
      networkRepository,
      routeRepository,
      nodeRepository,
      nodeAnalyzer
    )

    private val nodeChangeBuilder: NodeChangeBuilder = new NodeChangeBuilderImpl(
      analysisContext,
      nodeRepository,
      nodeLoader,
      nodeAnalyzer
    )

    private val tileChangeAnalyzer = new TileChangeAnalyzerImpl(
      taskRepository,
      routeTileCalculator
    )

    private val routeChangeBuilder: RouteChangeBuilder = new RouteChangeBuilderImpl(
      analysisContext,
      routeRepository,
      tileChangeAnalyzer
    )

    private val routesLoader = new RoutesLoaderSyncImpl(
      routeLoader
    )

    val changeBuilder: ChangeBuilder = new ChangeBuilderImpl(
      analysisContext,
      routesLoader,
      masterRouteAnalyzer,
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
          watchedProcessor
        )

        new NetworkCreateProcessorSyncImpl(
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
          networkLoader,
          networkUpdateNetworkProcessor
        )

        new NetworkUpdateProcessorSyncImpl(
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

        new NetworkDeleteProcessorSyncImpl(
          worker
        )
      }

      new NetworkChangeProcessorImpl(
        networkChangeAnalyzer,
        //  networkCreateProcessor,
        //  networkUpdateProcessor,
        //  networkDeleteProcessor,
        null
      )
    }

    private val changeSetInfoUpdater = new ChangeSetInfoUpdaterImpl(
      changeSetInfoRepository,
      taskRepository
    )

    private val routeChangeProcessor: RouteChangeProcessor = {

      val routeChangeAnalyzer = new RouteChangeAnalyzer(
        analysisContext,
        blackListRepository,
        elementIdAnalyzer
      )

      val createProcessor: RouteCreateProcessor = null
      val updateProcessor: RouteUpdateProcessor = null
      val deleteProcessor: RouteDeleteProcessor = null

      new RouteChangeProcessorImpl(
        analysisContext,
        routeChangeAnalyzer,
        createProcessor,
        updateProcessor,
        deleteProcessor,
        overpassRepository,
        masterRouteAnalyzer,
        tileChangeAnalyzer,
        routeRepository,
        analysisExecutionContext
      )
    }

    val nodeChangeAnalyzer = new NodeChangeAnalyzerImpl(
      analysisContext,
      blackListRepository
    )

    val bulkNodeAnalyzer: BulkNodeAnalyzer = new BulkNodeAnalyzerImpl(
      database,
      overpassRepository,
      nodeAnalyzer
    )

    private val nodeChangeProcessor = new NewNodeChangeProcessorImpl(
      analysisContext,
      bulkNodeAnalyzer,
      nodeChangeAnalyzer,
      nodeRepository
    )

    val changeProcessor: ChangeProcessor = {
      val changeSaver = new ChangeSaverImpl(
        changeSetRepository
      )

      new ChangeProcessor(
        routeChangeProcessor,
        networkChangeProcessor,
        null,
        nodeChangeProcessor,
        changeSetInfoUpdater,
        changeSaver
      )
    }


    private val fullNetworkAnalyzer: FullNetworkAnalyzer = new FullNetworkAnalyzerImpl(
      overpassRepository,
      networkRepository
    )

    private val fullRouteAnalyzer: FullRouteAnalyzer = new FullRouteAnalyzerImpl(
      overpassRepository,
      routeRepository,
      masterRouteAnalyzer,
      analysisExecutionContext: ExecutionContext
    )

    private val fullNodeAnalyzer: FullNodeAnalyzer = new FullNodeAnalyzerImpl(
      database,
      overpassRepository,
      nodeRepository,
      bulkNodeAnalyzer,
      analysisExecutionContext
    )

    private val networkInfoTagAnalyzer: NetworkInfoTagAnalyzer = new NetworkInfoTagAnalyzer()
    private val networkInfoRouteAnalyzer: NetworkInfoRouteAnalyzer = new NetworkInfoRouteAnalyzer(database)
    private val networkInfoNodeAnalyzer: NetworkInfoNodeAnalyzer = new NetworkInfoNodeAnalyzer(database)
    private val networkInfoFactAnalyzer: NetworkInfoFactAnalyzer = new NetworkInfoFactAnalyzer()
    private val networkInfoChangeAnalyzer: NetworkInfoChangeAnalyzer = new NetworkInfoChangeAnalyzer(database)
    private val networkCountryAnalyzer: NetworkCountryAnalyzer = new NetworkCountryAnalyzer(countryAnalyzer)

    private val networkInfoMasterAnalyzer: NetworkInfoMasterAnalyzer = new NetworkInfoMasterAnalyzer(
      database,
      networkInfoTagAnalyzer,
      networkInfoRouteAnalyzer,
      networkInfoNodeAnalyzer,
      networkInfoFactAnalyzer,
      networkInfoChangeAnalyzer,
      networkCountryAnalyzer
    )
    private val orphanNodeUpdater: OrphanNodeUpdater = new OrphanNodeUpdater(database)
    private val orphanRouteUpdater: OrphanRouteUpdater = new OrphanRouteUpdater(database)
    private val statisticsUpdater: StatisticsUpdater = new StatisticsUpdater(database)

    private val postProcessor: PostProcessor = new PostProcessor(
      networkInfoMasterAnalyzer,
      orphanNodeUpdater,
      orphanRouteUpdater,
      statisticsUpdater
    )

    private val fullAnalyzer = new FullAnalyzerImpl(
      fullNetworkAnalyzer,
      fullRouteAnalyzer,
      fullNodeAnalyzer,
      postProcessor
    )

    private val analysisDataInitializer: AnalysisDataInitializer = new AnalysisDataInitializerImpl(
      analysisContext,
      networkRepository,
      routeRepository,
      nodeRepository
    )

    /*
      Full analysis of the initial situation
     */
    def process(action: ChangeAction, element: RawElement): Unit = {
      fullAnalyzer.analyze(timestampBeforeValue)
      analysisDataInitializer.load()


      val changes = Seq(Change(action, Seq(element)))
      process(changes)
    }

    def process(changes: Seq[Change]): Unit = {
      val changeSet = newChangeSet(changes = changes)
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val changeSetContext = ChangeSetContext(ReplicationId(1), changeSet, elementIds)
      changeProcessor.process(changeSetContext)
    }

    def findRouteById(routeId: Long): RouteInfo = {
      database.routes.findById(routeId).getOrElse {
        val ids = database.routes.ids()
        if (ids.isEmpty) {
          fail(s"Could not find route $routeId, no routes in database")
        }
        else {
          fail(s"Could not find route $routeId (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findNodeById(nodeId: Long): NodeDoc = {
      database.nodes.findById(nodeId).getOrElse {
        val ids = database.nodes.ids()
        if (ids.isEmpty) {
          fail(s"Could not find node $nodeId, no nodes in database")
        }
        else {
          fail(s"Could not find route $nodeId (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findNetworkInfoById(networkId: Long): NetworkInfoDoc = {
      database.networkInfos.findById(networkId).getOrElse {
        val ids = database.networkInfos.ids()
        if (ids.isEmpty) {
          fail(s"Could not find NetworkInfoDoc $networkId, no networks in database")
        }
        else {
          fail(s"Could not find NetworkInfoDoc $networkId (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findChangeSetSummaryById(id: String): ChangeSetSummary = {
      database.changeSetSummaries.findByStringId(id).getOrElse {
        val ids = database.changeSetSummaries.stringIds()
        if (ids.isEmpty) {
          fail(s"Could not find changeSetSummary $id, no changeSetSummaries in database")
        }
        else {
          fail(s"Could not find changeSetSummary $id (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findNetworkChangeById(id: String): NetworkChange = {
      database.networkChanges.findByStringId(id).getOrElse {
        val ids = database.networkChanges.ids()
        if (ids.isEmpty) {
          fail(s"Could not find NetworkChange $id, no network changes in database")
        }
        else {
          fail(s"Could not find NetworkChange $id (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findRouteChangeById(id: String): RouteChange = {
      database.routeChanges.findByStringId(id).getOrElse {
        val ids = database.routeChanges.ids()
        if (ids.isEmpty) {
          fail(s"Could not find RouteChange $id, no route changes in database")
        }
        else {
          fail(s"Could not find RouteChange $id (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findNodeChangeById(id: String): NodeChange = {
      database.nodeChanges.findByStringId(id).getOrElse {
        val ids = database.nodeChanges.ids()
        if (ids.isEmpty) {
          fail(s"Could not find NodeChange $id, no node changes in database")
        }
        else {
          fail(s"Could not find NodeChange $id (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def beforeNodeWithId(nodeId: Long): Node = {
      before.nodes.getOrElse(
        nodeId,
        throw new IllegalArgumentException(s"No node with id $nodeId in test data")
      )
    }

    def beforeRelationWithId(relationId: Long): Relation = {
      before.relations.getOrElse(
        relationId,
        throw new IllegalArgumentException(s"No relation with id $relationId in before test data")
      )
    }

    def afterRelationWithId(relationId: Long): Relation = {
      after.relations.getOrElse(
        relationId,
        throw new IllegalArgumentException(s"No relation with id $relationId in after test data")
      )
    }

    @deprecated
    def watchNetwork(data: Data, networkId: Long): Unit = {
      analysisContext.data.networks.watched.add(networkId, RelationAnalyzer.toElementIds(data.relations(networkId)))
    }

    @deprecated
    def watchOrphanRoute(data: Data, routeId: Long): Unit = {
      val elementIds = RelationAnalyzer.toElementIds(data.relations(routeId))
      analysisContext.data.routes.watched.add(routeId, elementIds)
    }

    @deprecated
    def watchOrphanNode(nodeId: Long): Unit = {
      analysisContext.data.nodes.watched.add(nodeId)
    }

    @deprecated
    def relationBefore(data: Data, relationId: Long): Unit = {
      overpassQueryRelation(data, timestampBeforeValue, relationId)
    }

    @deprecated
    def relationAfter(data: Data, relationId: Long): Unit = {
      overpassQueryRelation(data, timestampAfterValue, relationId)
    }

    @deprecated
    def nodeBefore(data: Data, nodeId: Long): Unit = {
      overpassQueryNode(data, timestampBeforeValue, nodeId)
    }

    @deprecated
    def nodeAfter(data: Data, nodeId: Long): Unit = {
      overpassQueryNode(data, timestampAfterValue, nodeId)
    }

    @deprecated
    def nodesBefore(data: Data, nodeIds: Long*): Unit = {
      overpassQueryNodes(data, timestampBeforeValue, nodeIds)
    }

    @deprecated
    def nodesAfter(data: Data, nodeIds: Long*): Unit = {
      overpassQueryNodes(data, timestampAfterValue, nodeIds)
    }

    @deprecated
    private def overpassQueryRelation(data: Data, timestamp: Timestamp, relationId: Long): Unit = {
      val xml = toXml(RawDataSplitter.extractRelation(data.raw, relationId))
      overpassQuery(timestamp, QueryRelation(relationId), xml)
    }

    @deprecated
    private def overpassQueryNode(data: Data, timestamp: Timestamp, nodeId: Long): Unit = {
      val xml = toXml(RawData(Some(timestamp), data.raw.nodeWithId(nodeId).toSeq))
      overpassQuery(timestamp, QueryNode(nodeId), xml)
    }

    @deprecated
    def overpassQueryNodes(data: Data, timestamp: Timestamp, nodeIds: Seq[Long]): Unit = {
      val nodes = nodeIds.flatMap(nodeId => data.raw.nodeWithId(nodeId))
      val xml = toXml(RawData(Some(timestamp), nodes))
      overpassQuery(timestamp, QueryNodes("nodes", nodeIds), xml)
    }

    @deprecated
    private def overpassQuery(timestamp: Timestamp, query: OverpassQuery, xml: String): Unit = {
      (overpassQueryExecutor.executeQuery _).when(Some(timestamp), query).returns(xml).anyNumberOfTimes()
      ()
    }

    @deprecated
    private def toXml(data: RawData): String = {
      val sw = new StringWriter()
      val pw = new PrintWriter(sw)
      new OsmDataXmlWriter(data, pw).print()
      sw.toString
    }
  }
}
