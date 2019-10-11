package kpn.core.engine.changes.orphan.route

import kpn.core.engine.changes.AnalysisTestData
import kpn.core.engine.changes.ElementChanges
import kpn.server.repository.MockBlackListRepository
import kpn.core.test.TestData
import kpn.shared.SharedTestObjects
import kpn.shared.changes.Change
import kpn.shared.changes.Change.modify
import kpn.shared.data.raw.RawRelation
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanRouteChangeAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

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
    new OrphanRouteChangeAnalyzer(d.analysisContext, new MockBlackListRepository()).analyze(cs)
  }

  private def relationWithoutTags(relationId: Long): RawRelation = {
    new TestData() {
      relation(relationId)
    }.data.relations(relationId).raw
  }
}
