package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Change
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.mongo.doc.NodeDoc
import kpn.core.mongo.doc.OrphanNodeDoc
import kpn.core.mongo.doc.OrphanRouteDoc
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.test.OverpassData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdaterImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
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
import kpn.server.analyzer.engine.changes.ElementIdAnalyzerImpl
import kpn.server.analyzer.engine.changes.builder.ChangeBuilder
import kpn.server.analyzer.engine.changes.builder.ChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilder
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilder
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilderImpl
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.data.BlackList
import kpn.server.analyzer.engine.changes.network.NetworkChange
import kpn.server.analyzer.engine.changes.network.NetworkChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessorImpl
import kpn.server.analyzer.engine.changes.network.NetworkInfoChangeProcessorImpl
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzerImpl
import kpn.server.analyzer.engine.changes.node.NodeChangeProcessorImpl
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessorImpl
import kpn.server.analyzer.engine.context.AnalysisContext
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
import kpn.server.analyzer.load.NodeLoader
import kpn.server.analyzer.load.OldRouteLoader
import kpn.server.analyzer.load.RoutesLoaderSyncImpl
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

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

abstract class AbstractIntegrationTest extends UnitTest with MockFactory with SharedTestObjects {

  protected def node(data: Data, id: Long): RawNode = {
    data.raw.nodeWithId(id).get
  }

  protected def relation(data: Data, id: Long): RawRelation = {
    data.raw.relationWithId(id).get
  }

  protected class IntegrationTestContext(database: Database, dataBefore: OverpassData, dataAfter: OverpassData) {

    val before: Data = dataBefore.data
    val after: Data = dataAfter.data

    val analysisContext = new AnalysisContext()

    val overpassRepository: OverpassRepository = new OverpassRepositoryMock(before, after)
    implicit val analysisExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

    val countryAnalyzer: CountryAnalyzer = new CountryAnalyzerMock()
    val oldNodeAnalyzer: OldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val overpassQueryExecutor: OverpassQueryExecutor = stub[OverpassQueryExecutor]

    val changeSetRepository: ChangeSetRepository = new ChangeSetRepositoryImpl(database, null)
    val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)
    val routeRepository: RouteRepository = new RouteRepositoryImpl(database)
    val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database)
    val changeSetInfoRepository: ChangeSetInfoRepository = new ChangeSetInfoRepositoryImpl(database)

    private val taskRepository: TaskRepository = stub[TaskRepository]
    private val blackListRepository: BlackListRepository = stub[BlackListRepository]
    (() => blackListRepository.get).when().returns(BlackList())

    private val nodeLoader: NodeLoader = null
    private val routeLoader: OldRouteLoader = null
    private val tileCalculator = new TileCalculatorImpl()
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

    private val elementIdAnalyzer = new ElementIdAnalyzerImpl

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

      new NetworkChangeProcessorImpl(
        database,
        analysisContext,
        networkChangeAnalyzer,
        overpassRepository
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

      new RouteChangeProcessorImpl(
        analysisContext,
        routeChangeAnalyzer,
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

    private val nodeChangeProcessor = new NodeChangeProcessorImpl(
      analysisContext,
      bulkNodeAnalyzer,
      nodeChangeAnalyzer,
      nodeRepository
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

    val networkInfoChangeProcessor = new NetworkInfoChangeProcessorImpl(
      database,
      networkInfoMasterAnalyzer
    )

    val changeProcessor: ChangeProcessor = {
      val changeSaver = new ChangeSaverImpl(
        changeSetRepository
      )

      new ChangeProcessor(
        networkChangeProcessor,
        routeChangeProcessor,
        nodeChangeProcessor,
        networkInfoChangeProcessor,
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

    fullAnalyzer.analyze(timestampBeforeValue)
    analysisDataInitializer.load()

    def process(action: ChangeAction, element: RawElement): Unit = {
      val changes = Seq(Change(action, Seq(element)))
      process(changes)
      postProcessor.processPhase2()
    }

    def process(changes: Seq[Change]): Unit = {
      val changeSet = newChangeSet(changes = changes)
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val changeSetContext = ChangeSetContext(
        ReplicationId(1),
        changeSet,
        elementIds
      )
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

    def findOrphanRouteById(routeId: Long): OrphanRouteDoc = {
      database.orphanRoutes.findById(routeId).getOrElse {
        val ids = database.orphanRoutes.ids()
        if (ids.isEmpty) {
          fail(s"Could not find orphan route $routeId, no orphan routes in database")
        }
        else {
          fail(s"Could not find orphan route $routeId (but found: ${ids.mkString(", ")})")
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

    def findOrphanNodeById(_id: String): OrphanNodeDoc = {
      database.orphanNodes.findByStringId(_id).getOrElse {
        val ids = database.orphanNodes.stringIds()
        if (ids.isEmpty) {
          fail(s"Could not find orphan node ${_id}, no orphan nodes in database")
        }
        else {
          fail(s"Could not find orphan node ${_id} (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findNetworkById(networkId: Long): NetworkDoc = {
      database.networks.findById(networkId).getOrElse {
        val ids = database.networks.ids()
        if (ids.isEmpty) {
          fail(s"Could not find NetworkDoc $networkId, no networks in database")
        }
        else {
          fail(s"Could not find NetworkDoc $networkId (but found: ${ids.mkString(", ")})")
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
        val ids = database.networkChanges.stringIds()
        if (ids.isEmpty) {
          fail(s"Could not find NetworkChange $id, no network changes in database")
        }
        else {
          fail(s"Could not find NetworkChange $id (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findNetworkInfoChangeById(id: String): NetworkInfoChange = {
      database.networkInfoChanges.findByStringId(id).getOrElse {
        val ids = database.networkInfoChanges.stringIds()
        if (ids.isEmpty) {
          fail(s"Could not find NetworkInfoChange $id, no network info changes in database")
        }
        else {
          fail(s"Could not find NetworkInfoChange $id (but found: ${ids.mkString(", ")})")
        }
      }
    }

    def findRouteChangeById(id: String): RouteChange = {
      database.routeChanges.findByStringId(id).getOrElse {
        val ids = database.routeChanges.stringIds()
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
        val ids = database.nodeChanges.stringIds()
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
  }
}
