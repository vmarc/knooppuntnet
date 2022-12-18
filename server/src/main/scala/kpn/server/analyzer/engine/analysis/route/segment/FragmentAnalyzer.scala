package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.WayAnalyzer

object FragmentAnalyzer {
  private val log: Log = Log(classOf[FragmentAnalyzer])
}

/**
 * Splits up all way members in the route relation into fragments.
 * We split a way at a way node when this node is a network node, or when this node also is found in another way.
 *
 * @param routeNodes all network nodes that were found in the route relation or the route ways
 * @param wayMembers all members of type 'way' in the route relation
 */
class FragmentAnalyzer(routeNodes: Seq[RouteNode], wayMembers: Seq[WayMember]) {

  import FragmentAnalyzer.log

  private val routeNodeIds = routeNodes.map(_.id)

  private val nodeMap = log.infoElapsed("build node map", wayMembers.flatMap(wayMember => wayMember.way.nodes.map(node => node.id -> wayMember)).groupBy(_._1)).map(x => x._1 -> x._2.map(_._2))

  def fragmentMap: FragmentMap = {
    val fragments = wayMembers.flatMap { wayMember =>
      fragmentsIn(wayMember)
    }

    if (log.isDebugEnabled) {
      log.debug(s"${wayMembers.size} ways splitted up in ${fragments.size} fragments")
    }

    FragmentMap(fragments.distinct)
  }

  private def fragmentsIn(wayMember: WayMember): Seq[Fragment] = {

    if (wayMember.way.nodes.size < 3) {
      // a way with only 2 nodes cannot be split up any further
      Seq(fragment(wayMember, Vector.empty))
    }
    else {
      val splitIndexes = indexesAtWhichToSplit(wayMember)
      if (splitIndexes.isEmpty) {
        Seq(fragment(wayMember, Vector.empty))
      }
      else {
        splitIndexes.sliding(2).toSeq.map { case Seq(from, to) =>
          val subsetNodes = wayMember.way.nodes.slice(from, from + to - from + 1)
          fragment(wayMember, subsetNodes)
        }
      }
    }
  }

  private def indexesAtWhichToSplit(wayMember: WayMember): Seq[Int] = {
    val splitNodeIds = nodesAtWhichToSplit(wayMember)
    val indexes = wayMember.way.nodes.zipWithIndex.filter { case (node, index) =>
      splitNodeIds.contains(node.id)
    }.map { case (node, index) => index }
    if (indexes.isEmpty) {
      Seq.empty
    }
    else {
      (indexes.toSet ++ Set(0, wayMember.way.nodes.size - 1)).toSeq.sorted
    }
  }

  private def nodesAtWhichToSplit(wayMember: WayMember): Seq[Long] = {
    val candidateNodeIds = candidateSplitNodes(wayMember.way)
    if (candidateNodeIds.isEmpty) {
      Seq.empty
    }
    else {
      val nodesWithRouteNode = findNodesWithRouteNode(wayMember.way, candidateNodeIds)
      val nodesTouchingOtherWays = findNodesTouchingOtherWays(wayMember, candidateNodeIds)
      (nodesWithRouteNode ++ nodesTouchingOtherWays).distinct
    }
  }

  private def findNodesWithRouteNode(way: Way, candidateNodeIds: Seq[Long]): Seq[Long] = {
    val nodeIds = candidateNodeIds.filter(routeNodeIds.contains)
    if (log.isDebugEnabled) {
      if (nodeIds.nonEmpty) {
        log.debug(s"way ${way.id} splitted up at network nodes $nodeIds")
      }
    }
    nodeIds
  }

  private def findNodesTouchingOtherWays(wayMember: WayMember, candidateNodeIds: Seq[Long]): Seq[Long] = {
    candidateNodeIds.filter { nodeId =>
      nodeMap.get(nodeId) match {
        case Some(members) => members.exists(_ != wayMember)
        case None => false
      }
    }
  }

  private def candidateSplitNodes(way: Way): Seq[Long] = {
    if (isLoop(way)) {
      way.nodes.map(_.id) // all nodes
    }
    else {
      way.nodes.tail.dropRight(1).map(_.id) // all nodes except first and last
    }
  }

  private def isLoop(way: Way): Boolean = {
    WayAnalyzer.isRoundabout(way) || WayAnalyzer.isClosedLoop(way)
  }

  private def fragment(wayMember: WayMember, subsetNodes: Vector[Node]): Fragment = {
    val nodes = if (subsetNodes.isEmpty) wayMember.way.nodes else subsetNodes
    val start = routeNodes.find(routeNode => routeNode.id == nodes.head.id)
    val end = routeNodes.find(routeNode => routeNode.id == nodes.last.id)
    Fragment.create(
      start,
      end,
      wayMember.way,
      subsetNodes,
      wayMember.role
    )
  }
}
