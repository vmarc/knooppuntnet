package kpn.core.engine.changes

import kpn.core.changes.RelationAnalyzer.toElementIds
import kpn.core.test.TestData
import kpn.core.engine.changes.data.AnalysisData
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

  val ignoredNetwork = 2L
  val routeInIgnoredNetwork = 21L
  val wayInRouteInIgnoredNetwork = 201L
  val nodeInWayInIgnoredNetwork = 2001L
  val nodeInRouteInIgnoredNetwork = 2002L
  val nodeInIgnoredNetwork = 2003L

  val watchedOrphanRoute = 31L
  val wayInWatchedOrphanRoute = 301L
  val nodeInWayInWatchedOrphanRoute = 3001L
  val nodeInWatchedOrphanRoute = 3002L

  val ignoredOrphanRoute = 41L
  val wayInIgnoredOrphanRoute = 401L
  val nodeInWayInIgnoredOrphanRoute = 4001L
  val nodeInIgnoredOrphanRoute = 4002L

  val newOrphanRoute = 51L

  val newOrphanNode = 6001L
  val watchedOrphanNode = 6002L
  val ignoredOrphanNode = 6003L

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

    // ignored network

    networkNode(nodeInWayInIgnoredNetwork)
    networkNode(nodeInRouteInIgnoredNetwork)
    networkNode(nodeInIgnoredNetwork)

    way(wayInRouteInIgnoredNetwork, nodeInWayInIgnoredNetwork)

    route(routeInIgnoredNetwork, "",
      Seq(
        newMember("way", wayInRouteInIgnoredNetwork),
        newMember("node", nodeInRouteInIgnoredNetwork)
      )
    )

    networkRelation(
      ignoredNetwork,
      "",
      Seq(
        newMember("relation", routeInIgnoredNetwork),
        newMember("node", nodeInIgnoredNetwork)
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

    // ignored orphan route

    networkNode(nodeInWayInIgnoredOrphanRoute)
    networkNode(nodeInIgnoredOrphanRoute)

    way(wayInIgnoredOrphanRoute, nodeInWayInIgnoredOrphanRoute)

    route(ignoredOrphanRoute, "",
      Seq(
        newMember("way", wayInIgnoredOrphanRoute),
        newMember("node", nodeInIgnoredOrphanRoute)
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
    networkNode(ignoredOrphanNode)

  }.data


  // setup analysisData

  val analysisData = AnalysisData()
  analysisData.networks.watched.add(watchedNetwork, toElementIds(d.relations(watchedNetwork)))
  analysisData.networks.ignored.add(ignoredNetwork, toElementIds(d.relations(ignoredNetwork)))

  analysisData.orphanRoutes.watched.add(watchedOrphanRoute, toElementIds(d.relations(watchedOrphanRoute)))

  analysisData.orphanRoutes.ignored.add(ignoredOrphanRoute, toElementIds(d.relations(ignoredOrphanRoute)))

  analysisData.orphanNodes.watched.add(watchedOrphanNode)
  analysisData.orphanNodes.ignored.add(ignoredOrphanNode)

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
