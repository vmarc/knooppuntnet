package kpn.server.analyzer.engine.changes.network

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.AnalysisTestData
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.repository.MockBlackListRepository

import scala.language.reflectiveCalls

class NetworkChangeAnalyzerTest extends UnitTest with SharedTestObjects {

  val d = new AnalysisTestData()

  test("new network") {
    elementChanges(d.createRelation(d.newNetworkId)) should matchTo(ElementChanges(creates = Seq(d.newNetworkId)))
  }

  test("update of previously unknown network relation is treated as new network") {
    elementChanges(d.modifyRelation(d.newNetworkId)) should matchTo(ElementChanges(creates = Seq(d.newNetworkId)))
  }

  test("update existing network relation") {
    elementChanges(d.modifyRelation(d.watchedNetwork)) should matchTo(ElementChanges(updates = Seq(d.watchedNetwork)))
  }

  test("update existing network route") {
    elementChanges(d.modifyRelation(d.routeInWatchedNetwork)) should matchTo(ElementChanges(updates = Seq(d.watchedNetwork)))
    elementChanges(d.modifyWay(d.wayInRouteInWatchedNetwork)) should matchTo(ElementChanges(updates = Seq(d.watchedNetwork)))
    elementChanges(d.modifyNode(d.nodeInWayInWatchedNetwork)) should matchTo(ElementChanges(updates = Seq(d.watchedNetwork)))
    elementChanges(d.modifyNode(d.nodeInRouteInWatchedNetwork)) should matchTo(ElementChanges(updates = Seq(d.watchedNetwork)))

    // TODO make this work: elementChanges(d.modifyRelation(d.routeInIgnoredNetwork)) should equal(ElementChanges(updates = Seq(d.ignoredNetwork)))
    // TODO make this work: elementChanges(d.modifyWay(d.wayInRouteInIgnoredNetwork)) should equal(ElementChanges(updates = Seq(d.ignoredNetwork)))
    // TODO make this work: elementChanges(d.modifyNode(d.nodeInWayInIgnoredNetwork)) should equal(ElementChanges(updates = Seq(d.ignoredNetwork)))
    // TODO make this work: elementChanges(d.modifyNode(d.nodeInRouteInIgnoredNetwork)) should equal(ElementChanges(updates = Seq(d.ignoredNetwork)))
  }

  test("update existing network node") {
    elementChanges(d.modifyNode(d.nodeInWatchedNetwork)) should matchTo(ElementChanges(updates = Seq(d.watchedNetwork)))
    // TODO make this work: elementChanges(d.modifyNode(d.nodeInIgnoredNetwork)) should equal(ElementChanges(updates = Seq(d.ignoredNetwork)))
  }

  test("delete existing network") {
    elementChanges(d.deleteRelation(d.watchedNetwork)) should matchTo(ElementChanges(deletes = Seq(d.watchedNetwork)))
    // TODO make this work: elementChanges(d.deleteRelation(d.ignoredNetwork)) should equal(ElementChanges(deletes = Seq(d.ignoredNetwork)))
  }

  test("delete existing network because relation does not have the network tags anymore") {
    elementChanges(Change.delete(relationWithoutTags(d.watchedNetwork))) should matchTo(ElementChanges(deletes = Seq(d.watchedNetwork)))
    // TODO make this work? elementChanges(delete(relationWithoutTags(d.ignoredNetwork))) should equal(ElementChanges(deletes = Seq(d.ignoredNetwork)))
  }

  test("delete of unknown network does not have any effect") {
    elementChanges(d.deleteRelation(d.newNetworkId)) should matchTo(ElementChanges())
  }

  private def elementChanges(change: Change): ElementChanges = {
    val cs = newChangeSet(id = 1L, changes = Seq(change))
    new NetworkChangeAnalyzerImpl(d.analysisContext, new MockBlackListRepository()).analyze(cs)
  }

  private def relationWithoutTags(relationId: Long): RawRelation = {
    new TestData() {
      relation(relationId)
    }.data.relations(relationId).raw
  }
}
