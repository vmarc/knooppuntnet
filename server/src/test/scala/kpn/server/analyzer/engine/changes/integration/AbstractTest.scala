package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Country
import kpn.api.custom.Timestamp
import kpn.core.data.Data
import kpn.core.loadOld.OsmDataXmlWriter
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNode
import kpn.core.overpass.QueryNodes
import kpn.core.overpass.QueryRelation
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.OldMainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSaverImpl
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.builder.ChangeBuilder
import kpn.server.analyzer.engine.changes.builder.ChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilder
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilder
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilderImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
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
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileChangeAnalyzerImpl
import kpn.server.analyzer.load.NetworkLoader
import kpn.server.analyzer.load.NetworkLoaderImpl
import kpn.server.analyzer.load.NodeLoaderImpl
import kpn.server.analyzer.load.OldRouteLoaderImpl
import kpn.server.analyzer.load.RoutesLoaderSyncImpl
import kpn.server.analyzer.load.data.RawDataSplitter
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.AnalysisRepositoryImpl
import kpn.server.repository.BlackListRepository
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory

import java.io.PrintWriter
import java.io.StringWriter

abstract class AbstractTest extends UnitTest with MockFactory with SharedTestObjects {

  protected def node(data: Data, id: Long): RawNode = {
    data.raw.nodeWithId(id).get
  }

  protected def relation(data: Data, id: Long): RawRelation = {
    data.raw.relationWithId(id).get
  }

  protected class TestConfig() {

    val analysisContext = new AnalysisContext()

    val relationAnalyzer: RelationAnalyzer = new RelationAnalyzerImpl(analysisContext)

    val countryAnalyzer: CountryAnalyzer = new CountryAnalyzerMock(relationAnalyzer)
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

    private val nodeLoader = new NodeLoaderImpl(analysisContext, overpassQueryExecutor, countryAnalyzer, oldNodeAnalyzer)
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
    private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)

    private val oldNodeLocationAnalyzer = stub[OldNodeLocationAnalyzer]
    (oldNodeLocationAnalyzer.locations _).when(*, *).returns(Seq.empty)
    (oldNodeLocationAnalyzer.oldLocate _).when(*, *).returns(None)

    val oldMainNodeAnalyzer = new OldMainNodeAnalyzerImpl(
      countryAnalyzer,
      oldNodeLocationAnalyzer
    )

    val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(analysisContext, oldMainNodeAnalyzer, oldNodeAnalyzer)

    val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
      analysisContext,
      countryAnalyzer,
      relationAnalyzer,
      masterRouteAnalyzer
    )

    private val networkAnalyzer = new NetworkAnalyzerImpl(
      relationAnalyzer,
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
      relationAnalyzer,
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
      relationAnalyzer,
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
          changeBuilder,
          relationAnalyzer
        )

        new NetworkDeleteProcessorSyncImpl(
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
        nodeRepository,
        relationAnalyzer,
        routeRepository,
        masterRouteAnalyzer,
        nodeAnalyzer
      )

      val orphanRouteChangeAnalyzer = new OrphanRouteChangeAnalyzer(
        analysisContext,
        blackListRepository
      )
      new OrphanRouteChangeProcessorImpl(
        analysisContext,
        orphanRouteChangeAnalyzer,
        orphanRouteProcessor,
        routesLoader,
        masterRouteAnalyzer,
        routeRepository,
        relationAnalyzer,
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
        nodeRepository,
        nodeAnalyzer
      )

      val orphanNodeCreateProcessor = new OrphanNodeCreateProcessorImpl(
        analysisContext,
        nodeRepository,
        nodeAnalyzer
      )

      val orphanNodeUpdateProcessor = new OrphanNodeUpdateProcessorImpl(
        analysisContext,
        nodeRepository,
        nodeAnalyzer
      )

      new OrphanNodeChangeProcessorImpl(
        analysisContext,
        orphanNodeChangeAnalyzer,
        orphanNodeCreateProcessor,
        orphanNodeUpdateProcessor,
        orphanNodeDeleteProcessor,
        countryAnalyzer,
        oldNodeAnalyzer,
        nodeLoader
      )
    }

    val changeProcessor: ChangeProcessor = {
      val changeSaver = new ChangeSaverImpl(
        changeSetRepository
      )
      new ChangeProcessor(
        networkChangeProcessor,
        orphanRouteChangeProcessor,
        orphanNodeChangeProcessor,
        changeSetInfoUpdater,
        changeSaver
      )
    }

    def process(action: Int, element: RawElement): Unit = {
      val changes = Seq(Change(action, Seq(element)))
      process(changes)
    }

    def process(changes: Seq[Change]): Unit = {
      val changeSet = newChangeSet(changes = changes)
      val changeSetContext = ChangeSetContext(ReplicationId(1), changeSet)
      changeProcessor.process(changeSetContext)
    }

    def relationBefore(data: Data, relationId: Long): Unit = {
      overpassQueryRelation(data, timestampBeforeValue, relationId)
    }

    def relationAfter(data: Data, relationId: Long): Unit = {
      overpassQueryRelation(data, timestampAfterValue, relationId)
    }

    def nodeBefore(data: Data, nodeId: Long): Unit = {
      overpassQueryNode(data, timestampBeforeValue, nodeId)
    }

    def nodeAfter(data: Data, nodeId: Long): Unit = {
      overpassQueryNode(data, timestampAfterValue, nodeId)
    }

    def nodesBefore(data: Data, nodeIds: Long*): Unit = {
      overpassQueryNodes(data, timestampBeforeValue, nodeIds)
    }

    def nodesAfter(data: Data, nodeIds: Long*): Unit = {
      overpassQueryNodes(data, timestampAfterValue, nodeIds)
    }

    def watchNetwork(data: Data, networkId: Long): Unit = {
      analysisContext.data.networks.watched.add(networkId, relationAnalyzer.toElementIds(data.relations(networkId)))
    }

    def watchOrphanRoute(data: Data, routeId: Long): Unit = {
      val elementIds = relationAnalyzer.toElementIds(data.relations(routeId))
      analysisContext.data.orphanRoutes.watched.add(routeId, elementIds)
    }

    def watchOrphanNode(nodeId: Long): Unit = {
      analysisContext.data.orphanNodes.watched.add(nodeId)
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
}
