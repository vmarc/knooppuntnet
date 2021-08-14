package kpn.server.analyzer.engine.changes.route

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.ElementIdAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.data.BlackList
import kpn.server.analyzer.engine.changes.data.BlackListEntry
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.MockBlackListRepository

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class RouteChangeAnalyzerTest extends UnitTest with SharedTestObjects {

  test("'Create' route") {
    val setup = new Setup()
    val change = Change(Create, Seq(buildRoute(11L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        creates = Seq(11L)
      )
    )
  }

  test("'Modify' of previously unknown route relation is treated as new route") {
    val setup = new Setup()
    val change = Change(Modify, Seq(buildRoute(11L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        creates = Seq(11L)
      )
    )
  }

  test("'Modify' of existing route relation") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds())
    val change = Change(Modify, Seq(buildRoute(11L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        updates = Seq(11L)
      )
    )
  }

  test("'Modify' of existing route way") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds(wayIds = Set(101L)))
    val change = Change(Modify, Seq(newRawWay(101L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        updates = Seq(11L)
      )
    )
  }

  test("'Modify' of existing route node") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds(nodeIds = Set(1001L)))
    val change = Change(Modify, Seq(newRawNode(1001L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        updates = Seq(11L)
      )
    )
  }

  test("'Delete' known route") {
    val setup = new Setup()
    setup.analysisContext.watched.routes.add(11L, ElementIds())
    val change = Change(Delete, Seq(newRawRelation(11L)))
    setup.analyze(change) should matchTo(
      ElementChanges(
        deletes = Seq(11L)
      )
    )
  }

  test("Ignore 'Create' of blacklisted route") {
    val setup = new Setup()
    setup.blackListRoute(11L)
    val change = Change(Create, Seq(buildRoute(11L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Modify' of blacklisted route") {
    val setup = new Setup()
    setup.blackListRoute(11L)
    val change = Change(Modify, Seq(buildRoute(11L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Delete' of blacklisted route") {
    val setup = new Setup()
    setup.blackListRoute(11L)
    val change = Change(Delete, Seq(buildRoute(11L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Create' of non-route relation") {
    val setup = new Setup()
    val change = Change(Create, Seq(newRawRelation(11L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Modify' of non-route relation") {
    val setup = new Setup()
    val change = Change(Modify, Seq(newRawRelation(11L)))
    assert(setup.analyze(change).isEmpty)
  }

  test("Ignore 'Delete' of unknown route relation") {
    val setup = new Setup()
    val change = Change(Delete, Seq(newRawRelation(11L)))
    assert(setup.analyze(change).isEmpty)
  }

  private def buildRoute(routeId: Long, networkTagValue: String = "rwn"): RawRelation = {
    newRawRelation(
      routeId,
      tags = Tags.from(
        "network:type" -> "node_network",
        "type" -> "route",
        "network" -> networkTagValue
      )
    )
  }

  class Setup {

    val analysisContext = new AnalysisContext()
    private val blackListRepository = new MockBlackListRepository()

    def blackListRoute(routeId: Long): Unit = {
      blackListRepository.save(BlackList(routes = Seq(BlackListEntry(routeId, "", ""))))
    }

    implicit val analysisExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
    private val elementIdAnalyzer = new ElementIdAnalyzerImpl

    def analyze(change: Change): ElementChanges = {
      val changeSet = newChangeSet(changes = Seq(change))
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val context = ChangeSetContext(
        ReplicationId(1),
        changeSet,
        elementIds
      )
      new RouteChangeAnalyzer(analysisContext, blackListRepository, elementIdAnalyzer).analyze(context)
    }
  }
}
