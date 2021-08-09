package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.NodeInfo
import kpn.api.common.NodeName
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.data.Node
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.TestObjects
import kpn.core.mongo.doc.NodeDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerNoop
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.repository.NodeRepository
import org.scalamock.scalatest.MockFactory

class OrphanNodeDeleteProcessorTest extends UnitTest with MockFactory with TestObjects {

  test("deleted node is removed from analysis data watched orphan nodes, and set to non-active in the database") {

    val t = new Setup()

    val nodeId = 456L

    t.analysisContext.data.nodes.watched.add(nodeId)

    val context = newChangeSetContext()
    val loadedNodeDelete = LoadedNodeDelete(newRawNode(nodeId), None)

    t.processor.process(context, loadedNodeDelete)

    assert(!t.analysisContext.data.nodes.watched.contains(nodeId))

    (t.nodeRepository.save _).verify(
      where { nodeDoc: NodeDoc =>
        nodeDoc should matchTo(
          newNodeDoc(
            nodeId,
            labels = Seq(
              "orphan",
              "facts",
              "fact-Deleted"
            ),
            country = Some(Country.nl),
            active = false,
            // orphan = true,
            facts = Seq(Fact.Deleted)
          )
        )
        true
      }
    )
  }

  test("if loaded node did not exist before the changeset, the node info is saved based on info in delete, but no change is generated") {

    val t = new Setup()

    val nodeId = 456L

    val context = newChangeSetContext()
    val loadedNodeDelete = LoadedNodeDelete(newRawNode(nodeId), None)

    t.processor.process(context, loadedNodeDelete) should equal(None)

    (t.nodeRepository.save _).verify(
      where { nodeDoc: NodeDoc =>
        nodeDoc should matchTo(
          newNodeDoc(
            nodeId,
            labels = Seq(
              "orphan",
              "facts",
              "fact-Deleted"
            ),
            country = Some(Country.nl),
            active = false,
            // orphan = true,
            facts = Seq(Fact.Deleted)
          )
        )
        true
      }
    )
  }

  test("if loaded node did not exist before the changeset, and country is unknown, no action is done") {

    val t = new Setup(None)

    val nodeId = 456L

    val context = newChangeSetContext()
    val loadedNodeDelete = LoadedNodeDelete(newRawNode(nodeId), None)

    t.processor.process(context, loadedNodeDelete) should equal(None)

    (t.nodeRepository.save _).verify(*).never()
  }

  test("nodeChange is generated if node existed before changeset") {

    val t = new Setup()

    val nodeId = 456L
    val rawNode = newRawNode(nodeId, tags = Tags.from("rwn_ref" -> "01"))
    val context = newChangeSetContext()
    val loadedNode = LoadedNode(Some(Country.nl), Seq(NetworkType.hiking), "", Node(rawNode))
    val loadedNodeDelete = LoadedNodeDelete(rawNode, Some(loadedNode))

    t.processor.process(context, loadedNodeDelete).value should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = nodeId),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        name = "01",
        before = Some(rawNode),
        facts = Seq(Fact.WasOrphan, Fact.Deleted),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )

    (t.nodeRepository.save _).verify(
      where { nodeDoc: NodeDoc =>
        nodeDoc should matchTo(
          newNodeDoc(
            nodeId,
            labels = Seq(
              "orphan",
              "facts",
              "fact-Deleted",
              "network-type-hiking"
            ),
            active = false,
            // orphan = true,
            name = "01",
            names = Seq(
              NodeName(NetworkType.hiking, NetworkScope.regional, "01", None, proposed = false)
            ),
            country = Some(Country.nl),
            tags = Tags.from("rwn_ref" -> "01"),
            facts = Seq(Fact.Deleted)
          )
        )
        true
      }
    )
  }

  test("no nodeChange is generated if node does not belong to any known subset") {

    val t = new Setup(country = None)

    val nodeId = 456L
    val rawNode = newRawNode(nodeId)
    val context = newChangeSetContext()
    val loadedNode = LoadedNode(None, Seq(NetworkType.hiking), "", Node(rawNode))
    val loadedNodeDelete = LoadedNodeDelete(rawNode, Some(loadedNode))

    t.processor.process(context, loadedNodeDelete) should equal(None)

    (t.nodeRepository.save _).verify(
      where { nodeDoc: NodeDoc =>
        nodeDoc should matchTo(
          newNodeDoc(
            nodeId,
            labels = Seq(
              "orphan",
              "facts",
              "fact-Deleted"
            ),
            active = false,
            // orphan = true,
            country = None,
            facts = Seq(Fact.Deleted)
          )
        )
        true
      }
    )
  }

  private class Setup(country: Option[Country] = Some(Country.nl)) {

    val analysisContext: AnalysisContext = new AnalysisContext()
    val nodeRepository: NodeRepository = stub[NodeRepository]

    private val nodeAnalyzer: NodeAnalyzer = {
      new NodeAnalyzerImpl(
        new NodeCountryAnalyzerMock(country),
        new NodeTileAnalyzerNoop,
        new NodeLocationsAnalyzerNoop,
        new NodeRouteReferencesAnalyzerNoop
      )
    }

    val processor: OrphanNodeDeleteProcessor = new OrphanNodeDeleteProcessorImpl(
      analysisContext,
      nodeRepository,
      nodeAnalyzer
    )
  }
}
