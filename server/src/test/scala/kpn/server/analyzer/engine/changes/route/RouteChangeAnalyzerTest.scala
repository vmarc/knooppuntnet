package kpn.server.analyzer.engine.changes.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Change.modify
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.AnalysisTestData
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteChangeAnalyzer
import kpn.server.repository.MockBlackListRepository

class RouteChangeAnalyzerTest extends UnitTest with SharedTestObjects {

  val d = new AnalysisTestData()

  test("no result for 'Create' of a route that is referenced in network") {
    createRelation(d.routeInWatchedNetwork) should equal(ElementChanges())
  }

  test("no result for 'Modify' of route that is referenced in network") {
    modifyRelation(d.routeInWatchedNetwork) should equal(ElementChanges())
  }

  test("no result for 'Delete' of route that is referenced in network") {
    deleteRelation(d.routeInWatchedNetwork) should equal(ElementChanges())
  }

  test("'Create' of new orphan route") {
    createRelation(d.newOrphanRoute) should equal(ElementChanges(creates = Seq(d.newOrphanRoute)))
  }

  test("'Modify' of previously unknown route relation is treated as new orphan route") {
    modifyRelation(d.newOrphanRoute) should equal(ElementChanges(creates = Seq(d.newOrphanRoute)))
  }

  test("'Modify' of existing orphan route relation") {
    modifyRelation(d.watchedOrphanRoute) should equal(ElementChanges(updates = Seq(d.watchedOrphanRoute)))
  }

  test("'Modify' of existing orphan route way") {
    modifyWay(d.wayInWatchedOrphanRoute) should equal(ElementChanges(updates = Seq(d.watchedOrphanRoute)))
  }

  test("'Modify' of existing orphan route node") {
    modifyNode(d.nodeInWatchedOrphanRoute) should equal(ElementChanges(updates = Seq(d.watchedOrphanRoute)))
  }

  test("'Delete' existing orphan route") {
    deleteRelation(d.watchedOrphanRoute) should equal(ElementChanges(deletes = Seq(d.watchedOrphanRoute)))
  }

  test("'Delete' existing orphan route because relation does not have the route tags anymore") {
    elementChanges(modify(relationWithoutTags(d.watchedOrphanRoute))) should equal(ElementChanges(deletes = Seq(d.watchedOrphanRoute)))
  }

  test("'Delete' of unknown route does not have any effect") {
    deleteRelation(d.newOrphanRoute) should equal(ElementChanges())
  }

  private def createNode(nodeId: Long): ElementChanges = elementChanges(d.createNode(nodeId))

  private def modifyNode(nodeId: Long): ElementChanges = elementChanges(d.modifyNode(nodeId))

  private def deleteNode(nodeId: Long): ElementChanges = elementChanges(d.deleteNode(nodeId))

  private def createWay(wayId: Long): ElementChanges = elementChanges(d.createWay(wayId))

  private def modifyWay(wayId: Long): ElementChanges = elementChanges(d.modifyWay(wayId))

  private def deleteWay(wayId: Long): ElementChanges = elementChanges(d.deleteWay(wayId))

  private def createRelation(relationId: Long): ElementChanges = elementChanges(d.createRelation(relationId))

  private def modifyRelation(relationId: Long): ElementChanges = elementChanges(d.modifyRelation(relationId))

  private def deleteRelation(relationId: Long): ElementChanges = elementChanges(d.deleteRelation(relationId))

  private def elementChanges(change: Change): ElementChanges = {
    val cs = newChangeSet(changes = Seq(change))
    new RouteChangeAnalyzer(d.analysisContext, new MockBlackListRepository()).analyze(cs)
  }

  private def relationWithoutTags(relationId: Long): RawRelation = {
    new TestData() {
      relation(relationId)
    }.data.relations(relationId).raw
  }
}
