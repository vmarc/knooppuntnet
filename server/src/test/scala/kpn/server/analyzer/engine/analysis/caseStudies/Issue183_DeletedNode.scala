package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeSet
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawNode
import kpn.api.common.location.Location
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.OldNetworkNameAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.OldMainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.builder.ChangeBuilder
import kpn.server.analyzer.engine.changes.builder.ChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.NodeChangeBuilderImpl
import kpn.server.analyzer.engine.changes.builder.RouteChangeBuilderImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateNetworkProcessor
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateNetworkProcessorImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculator
import kpn.server.analyzer.engine.tile.TileChangeAnalyzer
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.load.NodeLoader
import kpn.server.analyzer.load.RoutesLoader
import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

import scala.xml.InputSource
import scala.xml.XML

class Issue183_DeletedNode extends UnitTest with MockFactory with SharedTestObjects {

  private val changeSetId = 104737699L
  private val replicationNumber = 4542690L
  private val deletedNodeId = 8731919671L
  private val replacementNodeId = 8734240777L

  test("rpn node removed in way in orphan rpn route") {

    val analysisBefore = CaseStudy.routeAnalysis("12713351-before")
    val deletedNodeBefore = analysisBefore.route.analysis.map.endNodes.head
    deletedNodeBefore.id should equal(deletedNodeId)
    deletedNodeBefore.name should equal("59")

    val analysisAfter = CaseStudy.routeAnalysis("12713351-after")
    val deletedNodeAfter = analysisAfter.route.analysis.map.endNodes.head
    deletedNodeAfter.id should equal(replacementNodeId)
    deletedNodeAfter.name should equal("59")

    val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

    val addedNode = routeUpdate.diffs.nodeDiffs.head.added.head
    val removedNode = routeUpdate.diffs.nodeDiffs.head.removed.head
    addedNode should equal(Ref(replacementNodeId, "59"))
    removedNode should equal(Ref(deletedNodeId, "59"))
  }

  test("rpn node removed in way in rmn route in rmn network") {

    val networkId = 8438300L

    val processor = buildNetworkUpdateNetworkProcessor()

    val context = buildChangeSetContext()
    val loadedNetworkBefore = loadNetwork("/case-studies/network-8438300-before.xml", networkId)
    val loadedNetworkAfter = loadNetwork("/case-studies/network-8438300-after.xml", networkId)

    loadedNetworkAfter.data.nodes.get(deletedNodeId).map(_.id) should equal(None)
    val deletedNode = loadedNetworkBefore.data.nodes(deletedNodeId)
    deletedNode.id should equal(deletedNodeId)
    deletedNode.tags.has("rpn_ref", "59") should equal(true) // rpn_ref in node in way in rmn route

    val changeSetChanges = processor.process(
      context,
      loadedNetworkBefore,
      loadedNetworkAfter
    )

    changeSetChanges.routeChanges.head.updatedWays.head.removedNodes.head.id should equal(deletedNodeId)
  }

  private def loadNetwork(filename: String, networkId: Long): LoadedNetwork = {
    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    val relation = data.relations(networkId)
    val networkName = new OldNetworkNameAnalyzer(relation).name
    LoadedNetwork(networkId, ScopedNetworkType.rmn, networkName, data, relation)
  }

  private def buildNetworkUpdateNetworkProcessor(): NetworkUpdateNetworkProcessor = {

    val analysisContext = new AnalysisContext()
    val analysisRepository = stub[AnalysisRepository]
    val nodeRepository = stub[NodeRepository]
    (nodeRepository.nodeRouteReferences _).when(*, *).returns(Seq.empty)
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
    val networkRelationAnalyzer = {
      new NetworkRelationAnalyzerImpl(
        relationAnalyzer,
        countryAnalyzer
      )
    }
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeTileCalculator = new RouteTileCalculator {
      def tiles(z: Int, tileRoute: TileDataRoute): Seq[Tile] = Seq.empty
    }
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)

    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val networkAnalyzer: NetworkAnalyzer = {
      val oldNodeLocationAnalyzer = new OldNodeLocationAnalyzer {
        def locations(latitude: String, longitude: String): Seq[String] = Seq.empty

        def oldLocate(latitude: String, longitude: String): Option[Location] = None
      }
      val oldMainNodeAnalyzer = {
        new OldMainNodeAnalyzerImpl(
          countryAnalyzer,
          oldNodeLocationAnalyzer
        )
      }
      val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(
        oldMainNodeAnalyzer,
        oldNodeAnalyzer
      )

      val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
        countryAnalyzer,
        routeAnalyzer
      )

      new NetworkAnalyzerImpl(
        relationAnalyzer,
        networkNodeAnalyzer,
        networkRouteAnalyzer
      )
    }

    val routeRepository = stub[RouteRepository]
    val tileChangeAnalyzer = stub[TileChangeAnalyzer]

    val changeBuilder: ChangeBuilder = {
      val routesLoader = new RoutesLoader {
        def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]] = Seq.empty
      }
      val routeChangeBuilder = new RouteChangeBuilderImpl(
        analysisContext,
        relationAnalyzer,
        routeRepository,
        tileChangeAnalyzer
      )

      val nodeAnalyzer: NodeAnalyzer = {
        new NodeAnalyzerImpl(
          new NodeCountryAnalyzerNoop,
          new NodeTileAnalyzerNoop,
          new NodeLocationsAnalyzerNoop,
          new NodeRouteReferencesAnalyzerNoop
        )
      }

      val nodeLoader = new NodeLoader {

        def oldLoadNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[LoadedNode] = Seq.empty

        def oldLoad2(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = Seq.empty

        def load(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = Seq.empty

      }

      val nodeChangeBuilder = new NodeChangeBuilderImpl(
        analysisContext,
        nodeRepository,
        nodeLoader,
        nodeAnalyzer
      )

      new ChangeBuilderImpl(
        analysisContext,
        routesLoader: RoutesLoader,
        routeAnalyzer,
        routeChangeBuilder,
        nodeChangeBuilder
      )
    }

    new NetworkUpdateNetworkProcessorImpl(
      analysisContext,
      analysisRepository,
      networkRelationAnalyzer,
      networkAnalyzer,
      changeBuilder
    )
  }

  private def buildChangeSetContext(): ChangeSetContext = {
    val changeSet = ChangeSet(
      changeSetId,
      timestamp = Timestamp(2021, 5, 15, 15, 8, 19),
      timestampFrom = Timestamp(2021, 5, 15, 15, 8, 18),
      timestampUntil = Timestamp(2021, 5, 15, 15, 8, 20),
      timestampBefore = Timestamp(2021, 5, 15, 15, 8, 18),
      timestampAfter = Timestamp(2021, 5, 15, 15, 8, 20),
      changes = Seq.empty
    )
    ChangeSetContext(
      ReplicationId(replicationNumber),
      changeSet
    )
  }
}
