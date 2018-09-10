package kpn.core.engine.changes.integration

import java.io.PrintWriter
import java.io.StringWriter

import kpn.core.changes.RelationAnalyzer
import kpn.core.data.Data
import kpn.core.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.country.CountryAnalyzerMock
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.engine.changes.ChangeProcessor
import kpn.core.engine.changes.ChangeSaverImpl
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.builder.ChangeBuilder
import kpn.core.engine.changes.builder.ChangeBuilderImpl
import kpn.core.engine.changes.builder.NodeChangeBuilder
import kpn.core.engine.changes.builder.NodeChangeBuilderImpl
import kpn.core.engine.changes.builder.RouteChangeBuilder
import kpn.core.engine.changes.builder.RouteChangeBuilderImpl
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.data.BlackList
import kpn.core.engine.changes.ignore.IgnoredNetworkAnalyzerImpl
import kpn.core.engine.changes.ignore.IgnoredNodeAnalyzerImpl
import kpn.core.engine.changes.network.NetworkChangeAnalyzerImpl
import kpn.core.engine.changes.network.NetworkChangeProcessorImpl
import kpn.core.engine.changes.network.create.NetworkCreateIgnoredProcessorImpl
import kpn.core.engine.changes.network.create.NetworkCreateProcessor
import kpn.core.engine.changes.network.create.NetworkCreateProcessorSyncImpl
import kpn.core.engine.changes.network.create.NetworkCreateProcessorWorkerImpl
import kpn.core.engine.changes.network.create.NetworkCreateWatchedProcessorImpl
import kpn.core.engine.changes.network.delete.NetworkDeleteProcessorSyncImpl
import kpn.core.engine.changes.network.delete.NetworkDeleteProcessorWorkerImpl
import kpn.core.engine.changes.network.update.NetworkUpdateCollectionProcessorImpl
import kpn.core.engine.changes.network.update.NetworkUpdateNetworkProcessorImpl
import kpn.core.engine.changes.network.update.NetworkUpdateProcessor
import kpn.core.engine.changes.network.update.NetworkUpdateProcessorSyncImpl
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
import kpn.core.load.NetworkLoader
import kpn.core.load.NetworkLoaderImpl
import kpn.core.load.NodeLoaderImpl
import kpn.core.load.RouteLoaderImpl
import kpn.core.load.RoutesLoaderSyncImpl
import kpn.core.load.data.RawDataSplitter
import kpn.core.loadOld.OsmDataXmlWriter
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNode
import kpn.core.overpass.QueryNodes
import kpn.core.overpass.QueryRelation
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.BlackListRepository
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NetworkRepository
import kpn.core.repository.NodeRepository
import kpn.core.repository.TaskRepository
import kpn.shared.ReplicationId
import kpn.shared.SharedTestObjects
import kpn.shared.Timestamp
import kpn.shared.changes.Change
import kpn.shared.data.raw.RawData
import kpn.shared.data.raw.RawElement
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

abstract class AbstractTest extends FunSuite with Matchers with MockFactory with SharedTestObjects {

  protected def node(data: Data, id: Long): RawNode = {
    data.raw.nodeWithId(id).get
  }

  protected def relation(data: Data, id: Long): RawRelation = {
    data.raw.relationWithId(id).get
  }

  protected class TestConfig() {

    val analysisData = AnalysisData()

    val countryAnalyzer: CountryAnalyzer = new CountryAnalyzerMock()
    val executor: OverpassQueryExecutor = stub[OverpassQueryExecutor]

    val analysisRepository: AnalysisRepository = stub[AnalysisRepository]
    val networkRepository: NetworkRepository = stub[NetworkRepository]
    val changeSetRepository: ChangeSetRepository = stub[ChangeSetRepository]
    val nodeRepository: NodeRepository = stub[NodeRepository]
    val changeSetInfoRepository: ChangeSetInfoRepository = stub[ChangeSetInfoRepository]
    private val taskRepository: TaskRepository = stub[TaskRepository]

    private val blackListRepository: BlackListRepository = stub[BlackListRepository]
    (blackListRepository.get _).when().returns(BlackList())

    private val nodeLoader = new NodeLoaderImpl(executor, executor, countryAnalyzer)
    private val routeLoader = new RouteLoaderImpl(executor, countryAnalyzer)
    private val networkLoader: NetworkLoader = new NetworkLoaderImpl(executor)
    private val routeAnalyzer = new MasterRouteAnalyzerImpl(new AccessibilityAnalyzerImpl())
    private val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)
    private val networkAnalyzer = new NetworkAnalyzerImpl(countryAnalyzer, routeAnalyzer)
    private val ignoredNetworkAnalyzer = new IgnoredNetworkAnalyzerImpl(countryAnalyzer)
    private val ignoredNodeAnalyzer = new IgnoredNodeAnalyzerImpl()

    private val nodeChangeBuilder: NodeChangeBuilder = new NodeChangeBuilderImpl(
      analysisData,
      analysisRepository,
      nodeLoader,
      ignoredNodeAnalyzer
    )

    private val routeChangeBuilder: RouteChangeBuilder = new RouteChangeBuilderImpl(
      analysisData,
      analysisRepository,
      countryAnalyzer,
      routeAnalyzer,
      routeLoader
    )

    private val routesLoader = new RoutesLoaderSyncImpl(
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

        val ignoredProcessor = new NetworkCreateIgnoredProcessorImpl(
          analysisData,
          analysisRepository,
          networkRelationAnalyzer
        )

        val worker = new NetworkCreateProcessorWorkerImpl(
          networkLoader,
          networkRelationAnalyzer,
          ignoredNetworkAnalyzer,
          watchedProcessor,
          ignoredProcessor
        )

        new NetworkCreateProcessorSyncImpl(
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

        val networkUpdateCollectionProcessor = new NetworkUpdateCollectionProcessorImpl(
          analysisData
        )

        val worker = new NetworkUpdateProcessorWorkerImpl(
          analysisData,
          analysisRepository,
          networkLoader,
          networkRelationAnalyzer,
          ignoredNetworkAnalyzer,
          networkUpdateNetworkProcessor,
          networkUpdateCollectionProcessor
        )

        new NetworkUpdateProcessorSyncImpl(
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
        ignoredNodeAnalyzer,
        countryAnalyzer
      )

      val orphanNodeCreateProcessor = new OrphanNodeCreateProcessorImpl(
        analysisData,
        analysisRepository,
        ignoredNodeAnalyzer
      )

      val orphanNodeUpdateProcessor = new OrphanNodeUpdateProcessorImpl(
        analysisData,
        analysisRepository,
        ignoredNodeAnalyzer
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
      val changeSaver = new ChangeSaverImpl(
        changeSetRepository
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
      analysisData.networks.watched.add(networkId, RelationAnalyzer.toElementIds(data.relations(networkId)))
    }

    def watchOrphanRoute(data: Data, routeId: Long): Unit = {
      val elementIds = RelationAnalyzer.toElementIds(data.relations(routeId))
      analysisData.orphanRoutes.watched.add(routeId, elementIds)
    }

    def ignoreOrphanRoute(data: Data, routeId: Long): Unit = {
      val elementIds = RelationAnalyzer.toElementIds(data.relations(routeId))
      analysisData.orphanRoutes.ignored.add(routeId, elementIds)
    }

    def watchOrphanNode(nodeId: Long): Unit = {
      analysisData.orphanNodes.watched.add(nodeId)
    }

    def ignoreOrphanNode(nodeId: Long): Unit = {
      analysisData.orphanNodes.ignored.add(nodeId)
    }

    private def overpassQueryRelation(data: Data, timestamp: Timestamp, relationId: Long): Unit = {
      val xml = toXml(RawDataSplitter.extractRelation(data.raw, relationId))
      overpassQuery(timestamp, QueryRelation(relationId), xml)
    }

    private def overpassQueryNode(data: Data, timestamp: Timestamp, nodeId: Long): Unit = {
      val xml = toXml(RawData(Some(timestamp), data.raw.nodeWithId(nodeId).toSeq))
      overpassQuery(timestamp, QueryNode(nodeId), xml)
    }

    private def overpassQueryNodes(data: Data, timestamp: Timestamp, nodeIds: Seq[Long]): Unit = {
      val nodes = nodeIds.flatMap(nodeId => data.raw.nodeWithId(nodeId))
      val xml = toXml(RawData(Some(timestamp), nodes))
      overpassQuery(timestamp, QueryNodes("nodes", nodeIds), xml)
    }

    private def overpassQuery(timestamp: Timestamp, query: OverpassQuery, xml: String): Unit = {
      (executor.executeQuery _).when(Some(timestamp), query).returns(xml).anyNumberOfTimes()
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
