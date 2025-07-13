package kpn.server.analyzer.engine.changes

import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.MemberType
import kpn.api.custom.Change
import kpn.core.test.TestData
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newRawNode
import kpn.core.test.TestObjects.newRawRelation
import kpn.core.test.TestObjects.newRawWay
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext

class AnalysisTestData {

  val watchedNetwork = 1L
  val routeInWatchedNetwork = 11L
  val wayInRouteInWatchedNetwork = 101L
  val nodeInWayInWatchedNetwork = 1001L
  val nodeInRouteInWatchedNetwork = 1002L
  val nodeInWatchedNetwork = 1003L

  val watchedOrphanRoute = 31L
  val wayInWatchedOrphanRoute = 301L
  val nodeInWayInWatchedOrphanRoute = 3001L
  val nodeInWatchedOrphanRoute = 3002L

  val newOrphanRoute = 51L

  val newOrphanNode = 6001L
  val watchedOrphanNode = 6002L

  val newNetworkId = 7L

  private val d = new TestData() {

    // new network

    networkRelation(
      newNetworkId,
      "",
      Seq.empty
    )

    // watched network

    networkNode(nodeInWayInWatchedNetwork)
    networkNode(nodeInRouteInWatchedNetwork)
    networkNode(nodeInWatchedNetwork)

    way(wayInRouteInWatchedNetwork, nodeInWayInWatchedNetwork)

    route(routeInWatchedNetwork, "",
      Seq(
        newMember(MemberType.Way, wayInRouteInWatchedNetwork),
        newMember(MemberType.Node, nodeInRouteInWatchedNetwork)
      )
    )

    networkRelation(
      watchedNetwork,
      "network1",
      Seq(
        newMember(MemberType.Relation, routeInWatchedNetwork),
        newMember(MemberType.Node, nodeInWatchedNetwork)
      )
    )

    // watched orphan route

    networkNode(nodeInWayInWatchedOrphanRoute)
    networkNode(nodeInWatchedOrphanRoute)

    way(wayInWatchedOrphanRoute, nodeInWayInWatchedOrphanRoute)

    route(watchedOrphanRoute, "",
      Seq(
        newMember(MemberType.Way, wayInWatchedOrphanRoute),
        newMember(MemberType.Node, nodeInWatchedOrphanRoute)
      )
    )

    // new orphan route

    route(newOrphanRoute, "",
      Seq(
      )
    )

    // nodes

    networkNode(newOrphanNode)
    networkNode(watchedOrphanNode)
  }.data

  // setup analysisData

  val analysisContext = new AnalysisContext()
  analysisContext.watched.networks.add(watchedNetwork)
  analysisContext.watched.routes.add(watchedOrphanRoute, RelationAnalyzer.toElementIds(d.relations(watchedOrphanRoute)))
  analysisContext.watched.nodes.add(watchedOrphanNode)

  def createNode(nodeId: Long): Change = Change(Create, Seq(d.nodes(nodeId).toRaw))

  def modifyNode(nodeId: Long): Change = Change(Modify, Seq(d.nodes(nodeId).toRaw))

  def deleteNode(nodeId: Long): Change = Change(Delete, Seq(newRawNode(nodeId)))

  def createWay(wayId: Long): Change = Change(Create, Seq(d.ways(wayId).toRaw))

  def modifyWay(wayId: Long): Change = Change(Modify, Seq(d.ways(wayId).toRaw))

  def deleteWay(wayId: Long): Change = Change(Delete, Seq(newRawWay(wayId)))

  def createRelation(relationId: Long): Change = Change(Create, Seq(d.relations(relationId).toRaw))

  def modifyRelation(relationId: Long): Change = Change(Modify, Seq(d.relations(relationId).toRaw))

  def deleteRelation(relationId: Long): Change = Change(Delete, Seq(newRawRelation(relationId)))
}
