package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newChange
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.TestObjects.newRawNode
import kpn.core.test.TestObjects.newRawRelation
import kpn.core.test.TestObjects.newRawWay
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.ElementIdAnalyzer
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.data.BlacklistEntry
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.BlacklistRepositoryMock

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class BaseRouteChangeAnalyzerTest extends UnitTest {

  test("'Create' route") {
    val setup = new Setup()
    val change = newChange(Create, relations = Seq(buildRoute(11L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        creates = Seq(11L)
      )
    )
  }

  test("'Modify' of previously unknown route relation is treated as new route") {
    val setup = new Setup()
    val change = newChange(Modify, relations = Seq(buildRoute(11L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        creates = Seq(11L)
      )
    )
  }

  test("'Modify' of existing route relation") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds())
    val change = newChange(Modify, relations = Seq(buildRoute(11L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        updates = Seq(11L)
      )
    )
  }

  test("'Modify' of existing route way") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds.from(wayIds = Set(101L)))
    val change = newChange(Modify, ways = Seq(newRawWay(101L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        updates = Seq(11L)
      )
    )
  }

  test("'Modify' of existing route node") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds.from(nodeIds = Set(1001L)))
    val change = newChange(Modify, nodes = Seq(newRawNode(1001L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        updates = Seq(11L)
      )
    )
  }

  test("'Delete' known route") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds())
    val change = newChange(Delete, relations = Seq(newRawRelation(11L)))
    assertEqual(
      setup.analyze(change),
      ElementChanges(
        deletes = Seq(11L)
      )
    )
  }

  test("Ignore 'Create' of blacklisted route") {
    val setup = new Setup()
    setup.blacklistRoute(11L)
    val change = newChange(Create, relations = Seq(buildRoute(11L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Modify' of blacklisted route") {
    val setup = new Setup()
    setup.blacklistRoute(11L)
    val change = newChange(Modify, relations = Seq(buildRoute(11L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Delete' of blacklisted route") {
    val setup = new Setup()
    setup.blacklistRoute(11L)
    val change = newChange(Delete, relations = Seq(buildRoute(11L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Create' of non-route relation") {
    val setup = new Setup()
    val change = newChange(Create, relations = Seq(newRawRelation(11L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Modify' of non-route relation") {
    val setup = new Setup()
    val change = newChange(Modify, relations = Seq(newRawRelation(11L)))
    setup.analyze(change) shouldBe empty
  }

  test("Ignore 'Delete' of unknown route relation") {
    val setup = new Setup()
    val change = newChange(Delete, relations = Seq(newRawRelation(11L)))
    setup.analyze(change) shouldBe empty
  }

  private def buildRoute(routeId: Long, networkTagValue: String = "rwn", routeTagValue: String = "hiking"): RawRelation = {
    newRawRelation(
      routeId,
      tags = Tags.from(
        "network:type" -> "node_network",
        "type" -> "route",
        "route" -> routeTagValue,
        "network" -> networkTagValue
      )
    )
  }

  class Setup {

    val analysisContext = new AnalysisContext()
    private val blacklistRepository = new BlacklistRepositoryMock()

    def blacklistRoute(routeId: Long): Unit = {
      blacklistRepository.save(Blacklist(routes = Seq(BlacklistEntry(routeId, "", ""))))
    }

    implicit val analysisExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
    private val elementIdAnalyzer = new ElementIdAnalyzer(analysisContext)

    def analyze(change: Change): ElementChanges = {
      val changeSet = newChangeSet(changes = Seq(change))
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val context = ChangeSetContext(
        ReplicationId(1),
        changeSet,
        elementIds
      )
      new BaseRouteChangeAnalyzer(analysisContext, blacklistRepository, elementIdAnalyzer).analyze(context)
    }
  }
}
