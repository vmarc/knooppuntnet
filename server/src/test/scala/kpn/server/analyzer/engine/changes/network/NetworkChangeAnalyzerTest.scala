package kpn.server.analyzer.engine.changes.network

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.AnalysisTestData
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.server.analyzer.engine.changes.data.BlackList
import kpn.server.analyzer.engine.changes.data.BlackListEntry
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.MockBlackListRepository

import scala.language.reflectiveCalls

class NetworkChangeAnalyzerTest extends UnitTest with SharedTestObjects {

  val d = new AnalysisTestData()

  test("'Create' network") {
    val setup = new Setup()
    val change = Change(Create, Seq(buildNetwork(1L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        creates = Seq(1L)
      )
    )
  }

  test("'Modify' of previously unknown network is treated as new network") {
    val setup = new Setup()
    val change = Change(Modify, Seq(buildNetwork(1L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        creates = Seq(1L)
      )
    )
  }

  test("'Modify' of known network relation") {
    val setup = new Setup()
    setup.analysisContext.data.networks.watched.add(1L, ElementIds())
    val change = Change(Modify, Seq(buildNetwork(1L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        updates = Seq(1L)
      )
    )
  }

  test("'Modify' of route in known network") {
    val setup = new Setup()
    setup.analysisContext.data.networks.watched.add(1L, ElementIds(relationIds = Set(11L)))
    val change = Change(Modify, Seq(newRawRelation(11L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        updates = Seq(1L)
      )
    )
  }

  test("'Modify' of node in known network") {
    val setup = new Setup()
    setup.analysisContext.data.networks.watched.add(1L, ElementIds(nodeIds = Set(1001L)))
    val change = Change(Modify, Seq(newRawNode(1001L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        updates = Seq(1L)
      )
    )
  }

  test("'Delete' known network") {
    val setup = new Setup()
    setup.analysisContext.data.networks.watched.add(1L, ElementIds())
    val change = Change(Delete, Seq(buildNetwork(1L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        deletes = Seq(1L)
      )
    )
  }

  test("Ignore 'Create' of blacklisted network") {
    val setup = new Setup()
    setup.blackListNetwork(1L)
    val change = Change(Create, Seq(buildNetwork(1L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Modify' of blacklisted network") {
    val setup = new Setup()
    setup.blackListNetwork(1L)
    val change = Change(Modify, Seq(buildNetwork(1L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Delete' of blacklisted network") {
    val setup = new Setup()
    setup.blackListNetwork(1L)
    val change = Change(Delete, Seq(buildNetwork(1L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Create' of non-network relation") {
    val setup = new Setup()
    val change = Change(Create, Seq(newRawRelation(1L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Modify' of non-network relation") {
    val setup = new Setup()
    val change = Change(Modify, Seq(newRawRelation(1L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Delete' of unknown network relation") {
    val setup = new Setup()
    val change = Change(Delete, Seq(newRawRelation(1L)))
    assert(setup.analyze(change).isEmpty)
  }

  private def buildNetwork(networkId: Long, networkTagValue: String = "rwn"): RawRelation = {
    newRawRelation(
      networkId,
      tags = Tags.from(
        "network:type" -> "node_network",
        "type" -> "network",
        "network" -> networkTagValue
      )
    )
  }

  class Setup {

    val analysisContext = new AnalysisContext()
    private val blackListRepository = new MockBlackListRepository()

    def blackListNetwork(networkId: Long): Unit = {
      blackListRepository.save(BlackList(networks = Seq(BlackListEntry(networkId, "", ""))))
    }

    def analyze(change: Change): ElementChanges = {
      val changeSet = newChangeSet(changes = Seq(change))
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val context = ChangeSetContext(
        ReplicationId(1),
        changeSet,
        elementIds
      )
      new NetworkChangeAnalyzerImpl(analysisContext, blackListRepository).analyze(context)
    }
  }

}
