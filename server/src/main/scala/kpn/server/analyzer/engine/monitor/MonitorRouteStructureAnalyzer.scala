package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.RouteWay

import scala.collection.mutable

class MonitorRouteStructureAnalyzer {

  def analyzeRoute(relation: Relation) {
    val mainSegments = analyzeMainSegments(relation)
    val forwardSegments = analyzeForwardSegments(relation)

    debug(s"main segments.size=${mainSegments.size}")
    debug(s"forward segments.size=${forwardSegments.size}")

    forwardSegments.zipWithIndex.foreach { case (forwardSegment, forwardSegmentIndex) =>
      debug(s"forward segment $forwardSegmentIndex")
      val startNodeId = forwardSegment.head.startNode.id
      val endNodeId = forwardSegment.last.endNode.id
      mainSegments.zipWithIndex.foreach { case (mainSegment, mainSegmentIndex) =>
        debug(s"  test main segment $mainSegmentIndex")
        val mainSegmentNodeIds = mainSegment.flatMap(_.way.nodes).map(_.id)
        if (mainSegmentNodeIds.contains(startNodeId) || mainSegmentNodeIds.contains(endNodeId)) {
          debug(s"forward segment $forwardSegmentIndex matched $mainSegmentIndex")
        }
      }
    }
  }

  private def analyzeMainSegments(relation: Relation): Seq[Seq[RouteWay]] = {

    val ways = relation
      .members
      .flatMap(member => member match {
        case wayMember: WayMember => Some(wayMember)
        case _ => None
      })
      .filter(wayMember => !wayMember.role.isDefined)
      .map(_.way)

    debug(s"main segments ways.size=${ways.size}")

    val segments = analyzeWaySegments(ways)
    debug(s"main segments segments.size=${segments.size}")
    segments.toSeq
  }

  private def analyzeForwardSegments(relation: Relation): Seq[Seq[RouteWay]] = {

    val wayMembers = relation
      .members
      .flatMap(member => member match {
        case wayMember: WayMember => Some(wayMember)
        case _ => None
      })

    val segments = mutable.Buffer[Seq[RouteWay]]()
    val wayMemberIterator = wayMembers.iterator

    if (wayMembers.isEmpty) {
      // done
    }
    else if (wayMembers.size == 1) {
      val wayMember = wayMemberIterator.next()
      if (wayMember.role.contains("forward")) {
        segments.addOne(Seq(RouteWay(wayMember.way)))
      }
    }
    else {
      val currentSegment = mutable.Buffer[Way]()
      var inForwardWays = false
      val wayMember = wayMemberIterator.next()
      if (wayMember.role.contains("forward")) {
        currentSegment.addOne(wayMember.way)
        inForwardWays = true
      }

      while (wayMemberIterator.hasNext) {
        val wayMember = wayMemberIterator.next()
        // debug(s"way ${wayMember.way.id} ${wayMember.role}")
        if (wayMember.role.contains("forward")) {
          if (inForwardWays) {
            // debug("add way to existing current segment")
            currentSegment.addOne(wayMember.way)
          }
          else {
            // debug("add first way to new current segment")
            currentSegment.addOne(wayMember.way)
            inForwardWays = true
          }
        }
        else {
          if (inForwardWays) {
            // debug("first way after forward segment, save to segments, and start new segment")
            segments.addAll(analyzeWaySegments(currentSegment.toSeq))
            currentSegment.clear()
            inForwardWays = false
          }
        }
      }

      if (currentSegment.nonEmpty) {
        segments.addAll(analyzeWaySegments(currentSegment.toSeq))
      }
    }
    segments.toSeq
  }


  private def analyzeWaySegments(ways: Seq[Way]): Seq[Seq[RouteWay]] = {

    val segments = mutable.Buffer[Seq[RouteWay]]()
    val wayIterator = ways.iterator

    if (ways.isEmpty) {
      // done
    }
    else if (ways.size == 1) {
      segments.addOne(Seq(RouteWay(ways.head)))
    }
    else {
      val way1 = wayIterator.next()
      val way2 = wayIterator.next()

      val routeWays = firstConnection(way1, way2)
      if (routeWays.isEmpty) {
        segments.addOne(Seq(RouteWay(way1)))
        segments.addOne(Seq(RouteWay(way2)))
      }
      else {
        val currentSegment = mutable.Buffer[RouteWay]()
        currentSegment.addAll(routeWays)

        // endNodeId: handle way without nodes or only one node
        var endNodeId = routeWays.last.endNode.id

        while (wayIterator.hasNext) {
          val way = wayIterator.next()
          if (endNodeId == way.nodes.head.id) {
            currentSegment.addOne(RouteWay(way))
            endNodeId = way.nodes.last.id
          }
          else if (endNodeId == way.nodes.last.id) {
            currentSegment.addOne(RouteWay(way, reversed = true))
            endNodeId = way.nodes.head.id
          }
          else {
            if (currentSegment.nonEmpty) {
              segments.addOne(currentSegment.toSeq)
              currentSegment.clear()
            }
          }
        }

        if (currentSegment.nonEmpty) {
          segments.addOne(currentSegment.toSeq)
        }
      }
    }

    debug(s"way segments.size=${segments.size}")
    segments.toSeq
  }

  private def firstConnection(way1: Way, way2: Way): Seq[RouteWay] = {
    if (way1.nodes.last.id == way2.nodes.head.id) {
      Seq(
        RouteWay(way1),
        RouteWay(way2)
      )
    }
    else if (way1.nodes.last.id == way2.nodes.last.id) {
      Seq(
        RouteWay(way1),
        RouteWay(way2, reversed = true)
      )
    }
    else if (way1.nodes.head.id == way2.nodes.head.id) {
      Seq(
        RouteWay(way1, reversed = true),
        RouteWay(way2)
      )
    }
    else if (way1.nodes.head.id == way2.nodes.last.id) {
      Seq(
        RouteWay(way1, reversed = true),
        RouteWay(way2, reversed = true)
      )
    }
    else {
      Seq.empty
    }
  }

  private def debug(message: String): Unit = {
    println(message)
  }
}
