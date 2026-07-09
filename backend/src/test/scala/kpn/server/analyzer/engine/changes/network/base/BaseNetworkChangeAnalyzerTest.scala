package kpn.server.analyzer.engine.changes.network.base

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newChange
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.TestObjects.newRawRelation
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.AnalysisTestData
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.data.BlacklistEntry
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlacklistRepositoryMock

class BaseNetworkChangeAnalyzerTest extends UnitTest {

  val d = new AnalysisTestData()

  test("'Create' network") {
    val setup = new Setup()
    val change = newChange(Create, relations = Seq(buildNetwork(1L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        creates = Seq(1L)
      )
    )
  }

  test("'Modify' of previously unknown network is treated as new network") {
    val setup = new Setup()
    val change = newChange(Modify, relations = Seq(buildNetwork(1L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        creates = Seq(1L)
      )
    )
  }

  test("'Modify' of known network relation") {
    val setup = new Setup()
    setup.analysisContext.watched.networks.add(1L)
    val change = newChange(Modify, relations = Seq(buildNetwork(1L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        updates = Seq(1L)
      )
    )
  }

  test("'Delete' known network") {
    val setup = new Setup()
    setup.analysisContext.watched.networks.add(1L)
    val change = newChange(Delete, relations = Seq(buildNetwork(1L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        deletes = Seq(1L)
      )
    )
  }

  test("Ignore 'Create' of blacklisted network") {
    val setup = new Setup()
    setup.blacklistNetwork(1L)
    val change = newChange(Create, relations = Seq(buildNetwork(1L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Modify' of blacklisted network") {
    val setup = new Setup()
    setup.blacklistNetwork(1L)
    val change = newChange(Modify, relations = Seq(buildNetwork(1L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Delete' of blacklisted network") {
    val setup = new Setup()
    setup.blacklistNetwork(1L)
    val change = newChange(Delete, relations = Seq(buildNetwork(1L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Create' of non-network relation") {
    val setup = new Setup()
    val change = newChange(Create, relations = Seq(newRawRelation(1L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Modify' of non-network relation") {
    val setup = new Setup()
    val change = newChange(Modify, relations = Seq(newRawRelation(1L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Delete' of unknown network relation") {
    val setup = new Setup()
    val change = newChange(Delete, relations = Seq(newRawRelation(1L)))
    setup.analyze(change) shouldBe empty
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
    private val blacklistRepository = new BlacklistRepositoryMock()

    def blacklistNetwork(networkId: Long): Unit = {
      blacklistRepository.save(Blacklist(networks = Seq(BlacklistEntry(networkId, "", ""))))
    }

    def analyze(change: Change): ElementChanges = {
      val changeSet = newChangeSet(changes = Seq(change))
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val context = ChangeSetContext(
        ReplicationId(1),
        changeSet,
        elementIds
      )
      new BaseNetworkChangeAnalyzer(analysisContext, blacklistRepository).analyze(context)
    }
  }
}
