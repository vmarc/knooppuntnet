package kpn.server.analyzer.engine.changes.node

import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.data.BlackList
import kpn.server.analyzer.engine.changes.data.BlackListEntry
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.MockBlackListRepository

class NodeChangeAnalyzerTest extends UnitTest with SharedTestObjects {

  test("'Create' new node") {
    val setup = new Setup()
    val change = Change(Create, Seq(createNode(1001L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        creates = Seq(1001L)
      )
    )
  }

  test("'Modify' of a previously unknown node is treated as new node") {
    val setup = new Setup()
    val change = Change(Modify, Seq(createNode(1001L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        creates = Seq(1001L)
      )
    )
  }

  test("'Modify' of an existing node") {
    val setup = new Setup()
    setup.analysisContext.data.nodes.watched.add(1001L)
    val change = Change(Modify, Seq(createNode(1001L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        updates = Seq(1001L)
      )
    )
  }

  test("'Delete' of an existing node") {
    val setup = new Setup()
    setup.analysisContext.data.nodes.watched.add(1001L)
    val change = Change(Delete, Seq(newRawNode(1001L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        deletes = Seq(1001L)
      )
    )
  }

  test("'Delete' of unknown node with tags in delete (does not happen in practice?)") {
    val setup = new Setup()
    val change = Change(Delete, Seq(createNode(1001L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        deletes = Seq(1001L)
      )
    )
  }

  test("'Delete' of unknown node without tags is ignored") {
    val setup = new Setup()
    val change = Change(Delete, Seq(newRawNode(1001L)))
    setup.analyze(change) should matchTo(ElementChanges())
  }

  test("Ignore 'Create' of blacklisted route") {
    val setup = new Setup()
    setup.blackListNode(1001L)
    val change = Change(Create, Seq(createNode(1001L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Modify' of blacklisted route") {
    val setup = new Setup()
    setup.blackListNode(1001L)
    val change = Change(Modify, Seq(createNode(1001L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Delete' of blacklisted route") {
    val setup = new Setup()
    setup.blackListNode(1001L)
    val change = Change(Delete, Seq(createNode(1001L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Create' of non-network node") {
    val setup = new Setup()
    val change = Change(Create, Seq(newRawNode(1001L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Modify' of non-network node") {
    val setup = new Setup()
    val change = Change(Modify, Seq(newRawNode(1001L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Delete' of non-network node") {
    val setup = new Setup()
    val change = Change(Delete, Seq(newRawNode(1001L)))
    assert(setup.analyze(change).isEmpty)
  }

  private def createNode(nodeId: Long, networkTagValue: String = "rwn_ref"): RawNode = {
    newRawNode(
      nodeId,
      tags = Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "01"
      )
    )
  }

  class Setup {

    val analysisContext = new AnalysisContext()
    private val blackListRepository = new MockBlackListRepository()

    def blackListNode(nodeId: Long): Unit = {
      blackListRepository.save(BlackList(nodes = Seq(BlackListEntry(nodeId, "", ""))))
    }

    def analyze(change: Change): ElementChanges = {
      val changeSet = newChangeSet(changes = Seq(change))
      new NodeChangeAnalyzerImpl(analysisContext, blackListRepository).analyze(changeSet)
    }
  }
}
