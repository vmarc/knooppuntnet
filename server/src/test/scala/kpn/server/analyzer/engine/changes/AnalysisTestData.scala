package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.core.test.TestData
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.shared.SharedTestObjects
import kpn.shared.changes.Change
import kpn.shared.changes.Change.create
import kpn.shared.changes.Change.delete
import kpn.shared.changes.Change.modify

class AnalysisTestData extends SharedTestObjects {

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
      Seq()
    )

    // watched network

    networkNode(nodeInWayInWatchedNetwork)
    networkNode(nodeInRouteInWatchedNetwork)
    networkNode(nodeInWatchedNetwork)

    way(wayInRouteInWatchedNetwork, nodeInWayInWatchedNetwork)

    route(routeInWatchedNetwork, "",
      Seq(
        newMember("way", wayInRouteInWatchedNetwork),
        newMember("node", nodeInRouteInWatchedNetwork)
      )
    )

    networkRelation(
      watchedNetwork,
      "network1",
      Seq(
        newMember("relation", routeInWatchedNetwork),
        newMember("node", nodeInWatchedNetwork)
      )
    )

    // watched orphan route

    networkNode(nodeInWayInWatchedOrphanRoute)
    networkNode(nodeInWatchedOrphanRoute)

    way(wayInWatchedOrphanRoute, nodeInWayInWatchedOrphanRoute)

    route(watchedOrphanRoute, "",
      Seq(
        newMember("way", wayInWatchedOrphanRoute),
        newMember("node", nodeInWatchedOrphanRoute)
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
  private val relationAnalyzer: RelationAnalyzer = new RelationAnalyzerImpl(analysisContext)

  analysisContext.data.networks.watched.add(watchedNetwork, relationAnalyzer.toElementIds(d.relations(watchedNetwork)))
  analysisContext.data.orphanRoutes.watched.add(watchedOrphanRoute, relationAnalyzer.toElementIds(d.relations(watchedOrphanRoute)))
  analysisContext.data.orphanNodes.watched.add(watchedOrphanNode)

  def createNode(nodeId: Long): Change = create(d.nodes(nodeId).raw)

  def modifyNode(nodeId: Long): Change = modify(d.nodes(nodeId).raw)

  def deleteNode(nodeId: Long): Change = delete(newRawNode(nodeId))

  def createWay(wayId: Long): Change = create(d.ways(wayId).raw)

  def modifyWay(wayId: Long): Change = modify(d.ways(wayId).raw)

  def deleteWay(wayId: Long): Change = delete(newRawWay(wayId))

  def createRelation(relationId: Long): Change = create(d.relations(relationId).raw)

  def modifyRelation(relationId: Long): Change = modify(d.relations(relationId).raw)

  def deleteRelation(relationId: Long): Change = delete(newRawRelation(relationId))

}
