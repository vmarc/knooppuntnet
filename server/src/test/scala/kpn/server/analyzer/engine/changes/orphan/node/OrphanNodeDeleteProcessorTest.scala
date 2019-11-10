package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.TestObjects
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.repository.AnalysisRepository
import kpn.core.test.TestData
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.data.Node
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanNodeDeleteProcessorTest extends FunSuite with Matchers with MockFactory with TestObjects {

  test("deleted node is removed from analysis data watched orphan nodes, and set to non-active in the database") {

    val t = new Setup()

    val nodeId = 456L

    t.analysisContext.data.orphanNodes.watched.add(nodeId)

    val context = newChangeSetContext()
    val loadedNodeDelete = LoadedNodeDelete(newRawNode(nodeId), None)

    t.processor.process(context, loadedNodeDelete)

    t.analysisContext.data.orphanNodes.watched.contains(nodeId) should equal(false)

    (t.analysisRepository.saveNode _).verify(
      newNodeInfo(
        nodeId,
        country = Some(Country.nl),
        active = false,
        orphan = true,
        facts = Seq(Fact.Deleted)
      )
    )
  }

  test("if loaded node did not exist before the changeset, the node info is saved based on info in delete, but no change is generated") {

    val t = new Setup()

    val nodeId = 456L

    val context = newChangeSetContext()
    val loadedNodeDelete = LoadedNodeDelete(newRawNode(nodeId), None)

    t.processor.process(context, loadedNodeDelete) should equal(None)

    (t.analysisRepository.saveNode _).verify(
      newNodeInfo(
        nodeId,
        country = Some(Country.nl),
        active = false,
        orphan = true,
        facts = Seq(Fact.Deleted)
      )
    )
  }

  test("if loaded node did not exist before the changeset, and country is unknown, no action is done") {

    val t = new Setup(None)

    val nodeId = 456L

    val context = newChangeSetContext()
    val loadedNodeDelete = LoadedNodeDelete(newRawNode(nodeId), None)

    t.processor.process(context, loadedNodeDelete) should equal(None)

    (t.analysisRepository.saveNode _).verify(*).never()
  }

  test("nodeChange is generated if node existed before changeset") {

    val t = new Setup()

    val nodeId = 456L
    val rawNode = newRawNode(nodeId)
    val context = newChangeSetContext()
    val loadedNode = LoadedNode(Some(Country.nl), Seq(NetworkType.hiking), "", Node(rawNode))
    val loadedNodeDelete = LoadedNodeDelete(rawNode, Some(loadedNode))

    t.processor.process(context, loadedNodeDelete) should equal(
      Some(
        newNodeChange(
          key = newChangeKey(elementId = nodeId),
          changeType = ChangeType.Delete,
          subsets = Seq(Subset.nlHiking),
          before = Some(rawNode),
          facts = Seq(Fact.WasOrphan, Fact.Deleted),
          investigate = true
        )
      )
    )

    (t.analysisRepository.saveNode _).verify(
      newNodeInfo(
        nodeId,
        active = false,
        orphan = true,
        country = Some(Country.nl),
        facts = Seq(Fact.Deleted)
      )
    ).once()
  }

  test("no nodeChange is generated if node does not belong to any known subset") {

    val t = new Setup()

    val nodeId = 456L
    val rawNode = newRawNode(nodeId)
    val context = newChangeSetContext()
    val loadedNode = LoadedNode(None, Seq(NetworkType.hiking), "", Node(rawNode))
    val loadedNodeDelete = LoadedNodeDelete(rawNode, Some(loadedNode))

    t.processor.process(context, loadedNodeDelete) should equal(None)

    (t.analysisRepository.saveNode _).verify(
      newNodeInfo(
        nodeId,
        active = false,
        orphan = true,
        country = None,
        facts = Seq(Fact.Deleted)
      )
    ).once()
  }

  private class Setup(country: Option[Country] = Some(Country.nl)) {

    val node: Node = new TestData() {
      networkNode(1001, "01")
    }.data.nodes(1001)

    val loadedNode: LoadedNode = LoadedNode(
      country = Some(Country.nl),
      networkTypes = Seq.empty,
      name = "01",
      node = node
    )

    val analysisContext: AnalysisContext = new AnalysisContext()
    val analysisRepository: AnalysisRepository = stub[AnalysisRepository]
    val countryAnalyzer: CountryAnalyzer = stub[CountryAnalyzer]

    (countryAnalyzer.country _).when(*).returns(country).anyNumberOfTimes()

    val processor: OrphanNodeDeleteProcessor = new OrphanNodeDeleteProcessorImpl(
      analysisContext,
      analysisRepository,
      countryAnalyzer
    )
  }

}
