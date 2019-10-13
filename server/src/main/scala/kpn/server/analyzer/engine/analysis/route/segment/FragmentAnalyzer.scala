package kpn.server.analyzer.engine.analysis.route.segment

import kpn.server.analyzer.engine.analysis.WayAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.core.util.Log
import kpn.shared.data.Node
import kpn.shared.data.Way
import kpn.shared.data.WayMember

object FragmentAnalyzer {
  val log = Log(classOf[FragmentAnalyzer])
}

/**
  * Splits up all way members in the route relation into fragments.
  * We split a way at a way node when this node is a network node, or when this node also is found in another way.
  *
  * @param routeNodes all network nodes that were found in the route relation or the route ways
  * @param wayMembers all members of type 'way' in the route relation
  */
class FragmentAnalyzer(routeNodes: Seq[RouteNode], wayMembers: Seq[WayMember]) {

  import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer.log

  private val routeNodeIds = routeNodes.map(_.id)

  def fragments: Seq[Fragment] = {
    val result = wayMembers.flatMap { wayMember =>
      fragmentsIn(wayMember)
    }

    if (log.isDebugEnabled) {
      log.debug(s"${wayMembers.size} ways splitted up in ${result.size} fragments")
    }

    result
  }

  private def fragmentsIn(wayMember: WayMember): Seq[Fragment] = {

    if (wayMember.way.nodes.size < 3) {
      // a way with only 2 nodes cannot be split up any further
      Seq(fragment(wayMember, Seq()))
    }
    else {
      val splitIndexes = indexesAtWhichToSplit(wayMember: WayMember)
      if (splitIndexes.isEmpty) {
        Seq(fragment(wayMember, Seq()))
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
    val splitNodes = nodesAtWhichToSplit(wayMember)
    val indexes = wayMember.way.nodes.zipWithIndex.filter { case (node, index) =>
      splitNodes.contains(node)
    }.map { case (node, index) => index }
    if (indexes.isEmpty) {
      Seq()
    }
    else {
      (indexes.toSet ++ Set(0, wayMember.way.nodes.size - 1)).toSeq.sorted
    }
  }

  private def nodesAtWhichToSplit(wayMember: WayMember): Set[Node] = {
    val candidates = candidateSplitNodes(wayMember.way)
    val nodesWithRouteNode = findNodesWithRouteNode(wayMember.way, candidates)
    val nodesTouchingOtherWays = findNodesTouchingOtherWays(wayMember, candidates)
    nodesWithRouteNode ++ nodesTouchingOtherWays
  }

  private def findNodesWithRouteNode(way: Way, candidates: Set[Node]): Set[Node] = {
    val nodes = candidates.filter(node => routeNodeIds.contains(node.id))
    if (log.isDebugEnabled) {
      if (nodes.nonEmpty) {
        log.debug(s"way ${way.id} splitted up at network nodes ${nodes.map(_.id)}")
      }
    }
    nodes
  }

  private def findNodesTouchingOtherWays(wayMember: WayMember, candidates: Set[Node]): Set[Node] = {
    val otherWayMembers = findOtherWayMembers(wayMember)
    otherWayMembers.flatMap { otherWayMember =>
      val otherWayNodeIds = otherWayMember.way.nodes.map(_.id).toSet
      val touchingNodeIds = candidates.filter(node => otherWayNodeIds.contains(node.id))
      if (log.isDebugEnabled) {
        if (touchingNodeIds.nonEmpty) {
          val wayId = wayMember.way.id
          val otherWayId = otherWayMember.way.id
          val touchingNodes = touchingNodeIds.mkString(",")
          //noinspection SideEffectsInMonadicTransformation
          log.debug(s"way $wayId splitted where it touches way $otherWayId at node(s): $touchingNodes")
        }
      }
      touchingNodeIds
    }.toSet
  }

  /*
   Finds all way members other than given waymember.
   Also excludes way members containing the same way as given member.
  */
  private def findOtherWayMembers(wayMember: WayMember): Seq[WayMember] = {
    wayMembers.filterNot(m => m.way.id == wayMember.way.id)
  }

  private def candidateSplitNodes(way: Way): Set[Node] = {
    if (isLoop(way)) {
      way.nodes.toSet // all nodes
    }
    else {
      way.nodes.tail.dropRight(1).toSet // all nodes except first and last
    }
  }

  private def isLoop(way: Way): Boolean = {
    WayAnalyzer.isRoundabout(way) || WayAnalyzer.isClosedLoop(way)
  }

  private def fragment(wayMember: WayMember, subsetNodes: Seq[Node]): Fragment = {
    val nodes = if (subsetNodes.isEmpty) wayMember.way.nodes else subsetNodes
    val start: Option[RouteNode] = routeNodes.find(routeNode => routeNode.id == nodes.head.id)
    val end: Option[RouteNode] = routeNodes.find(routeNode => routeNode.id == nodes.last.id)
    Fragment(start, end, wayMember.way, subsetNodes, wayMember.role)
  }
}
